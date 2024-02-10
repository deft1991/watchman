package com.deft.watchman.processor.commands.impl;

import com.deft.watchman.WatchmanApplication;
import com.deft.watchman.bot.WatchmanBot;
import com.deft.watchman.data.entity.postgres.ChatSettings;
import com.deft.watchman.processor.commands.impl.manage.BanWaitTimeCommandProcessor;
import com.deft.watchman.repository.postgres.ChatSettingsRepository;
import com.deft.watchman.testcontainer.TestContainerBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.api.objects.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WatchmanApplication.class)
@ActiveProfiles("test")
@ContextConfiguration(initializers = {TestContainerBase.Initializer.class})
@TestPropertySource(properties = "app.scheduling.enable=false")
class BanWaitTimeCommandProcessorTest extends TestContainerBase {

    @Autowired
    private BanWaitTimeCommandProcessor processor;

    @Autowired
    private ChatSettingsRepository chatSettingsRepository;

    @MockBean
    private WatchmanBot watchmanBot;

    @BeforeEach
    void setUp() {
        // Set up mock behavior if needed
    }

    @Test
    void testProcessCommand() {
        // Given
        Update update = new Update();
        Message message = new Message();

        User from = new User();
        from.setId(1L);
        from.setId(1L);

        Chat chat = new Chat();
        chat.setId(12345L);

        message.setChat(chat);
        message.setFrom(from);
        message.setText("100");

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setType("mention");
        messageEntity.setText("@name");
        message.setEntities(List.of(messageEntity));
        update.setMessage(message);

        ChatSettings chatSettings = new ChatSettings();
        chatSettings.setBanWaitTimeSeconds(0);
        chatSettings.setChatId(chat.getId());
        chatSettingsRepository.save(chatSettings);
        // Prepare mock behavior
        when(watchmanBot.isGroupAdmin(chat.getId(), from.getId())).thenReturn(true);


        // When
        processor.processCommand(watchmanBot, update, chatSettings);
        Optional<ChatSettings> optional = chatSettingsRepository.findByChatId(chat.getId());

        // Then
        assertTrue(optional.isPresent());
        assertEquals(Integer.valueOf(message.getText()), optional.get().getBanWaitTimeSeconds());
    }
}
