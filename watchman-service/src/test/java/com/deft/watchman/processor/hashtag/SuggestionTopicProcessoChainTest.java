package com.deft.watchman.processor.hashtag;

import com.deft.watchman.WatchmanApplication;
import com.deft.watchman.bot.WatchmanBot;
import com.deft.watchman.data.entity.postgres.SuggestTopic;
import com.deft.watchman.repository.postgres.SuggestTopicRepository;
import com.deft.watchman.service.SuggestTopicService;
import com.deft.watchman.testcontainer.TestContainerBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WatchmanApplication.class)
@ActiveProfiles("test")
@ContextConfiguration(initializers = {TestContainerBase.Initializer.class})
@TestPropertySource(properties = "app.scheduling.enable=false")
class SuggestionTopicProcessoChainTest extends TestContainerBase {

    @Autowired
    private SuggestTopicService service;

    @Autowired
    private SuggestTopicRepository repository;

    @MockBean
    private WatchmanBot watchmanBot;

    @BeforeEach
    void setUp() {
        // Set up mock behavior if needed
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "This is a valid #suggest_topic message",
            "#suggest_topic Valid message with tag",
            "Another valid message #suggest_topic"
    })
    void testProcessCommand(String messageText) {
        // Given
        SuggestionTopicProcessorChain processor = new SuggestionTopicProcessorChain(service);
        Update update = new Update();
        Message message = new Message();

        Chat chat = new Chat();
        chat.setId(12345L);

        message.setChat(chat);
        message.setText(messageText);

        update.setMessage(message);

        // Prepare mock behavior

        // When
        processor.check(message);
        Iterable<SuggestTopic> all = repository.findAll();

        // Then
        assertNotNull(all.iterator());
        assertTrue(all.iterator().hasNext());
    }
}
