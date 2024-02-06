package com.deft.watchman.processor.commands.impl;

import com.deft.watchman.WatchmanApplication;
import com.deft.watchman.bot.WatchmanBot;
import com.deft.watchman.data.entity.postgres.ChatSettings;
import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.repository.postgres.ChatUserRepository;
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
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WatchmanApplication.class)
@ActiveProfiles("test")
@ContextConfiguration(initializers = {TestContainerBase.Initializer.class})
@TestPropertySource(properties = "app.scheduling.enable=false")
class AddRatingCommandProcessorTest extends TestContainerBase {

    @Autowired
    private AddRatingCommandProcessor processor;

    @Autowired
    private ChatUserRepository chatUserRepository;

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
        Chat chat = new Chat();
        chat.setId(12345L);
        message.setChat(chat);
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setType("mention");
        messageEntity.setText("@name");
        message.setEntities(List.of(messageEntity));
        update.setMessage(message);
        // Add other necessary properties to the message object

        // Prepare mock behavior
        ChatUser chatUser = new ChatUser();
        chatUser.setUserName("name");
        chatUser.setChatId(12345L);
        chatUser.setRating(10);
        chatUserRepository.save(chatUser);

        ChatSettings chatSettings = new ChatSettings();

        // When
        processor.processCommand(watchmanBot, update, chatSettings);
        Optional<ChatUser> byUserNameAndChatId = chatUserRepository.findByUserNameAndChatId(chatUser.getUserName(), chatUser.getChatId());

        // Then
        assertTrue(byUserNameAndChatId.isPresent());
        assertEquals(chatUser.getRating() + 1, byUserNameAndChatId.get().getRating());
    }
}
