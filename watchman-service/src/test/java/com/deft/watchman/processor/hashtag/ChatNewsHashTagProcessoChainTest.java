package com.deft.watchman.processor.hashtag;

import com.deft.watchman.WatchmanApplication;
import com.deft.watchman.bot.WatchmanBot;
import com.deft.watchman.data.entity.postgres.ChatNews;
import com.deft.watchman.repository.postgres.ChatNewsRepository;
import com.deft.watchman.service.ChatNewsService;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WatchmanApplication.class)
@ActiveProfiles("test")
@ContextConfiguration(initializers = {TestContainerBase.Initializer.class})
@TestPropertySource(properties = "app.scheduling.enable=false")
class ChatNewsHashTagProcessoChainTest extends TestContainerBase {

    @Autowired
    private ChatNewsService service;

    @Autowired
    private ChatNewsRepository repository;

    @MockBean
    private WatchmanBot watchmanBot;

    @BeforeEach
    void setUp() {
        // Set up mock behavior if needed
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "This is a valid #news message",
            "#news Valid message with tag",
            "Another valid message #news"
    })
    void testProcessCommand(String messageText) {
        // Given
        ChatNewsHashTagProcessorChain processor = new ChatNewsHashTagProcessorChain(service);
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
        List<ChatNews> news = repository.findAllByCreateDateIsAfter(Instant.now().minus(2, ChronoUnit.MINUTES));

        // Then
        assertFalse(news.isEmpty());
    }
}
