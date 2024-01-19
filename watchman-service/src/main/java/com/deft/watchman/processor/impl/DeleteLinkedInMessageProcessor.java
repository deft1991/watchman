package com.deft.watchman.processor.impl;

import com.deft.watchman.data.entity.postgres.ChatSettings;
import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.processor.ChatUpdateProcessor;
import com.deft.watchman.processor.ProcessorType;
import com.deft.watchman.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 21.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteLinkedInMessageProcessor implements ChatUpdateProcessor {

    private final ChatUserService chatUserService;

    /**
     * Delete the Telegram's default message about user leaving
     */
    @Override
    public void processUpdate(AbilityBot bot, Update update, ChatSettings settings) {
        Message message = update.getEditedMessage();
        Chat chat = message.getChat();
        User fromUser = message.getFrom();
        Long userId = fromUser.getId();

        Optional<ChatUser> optionalChatUser = chatUserService.findByUserIdAndChatId(userId, chat.getId());
        if (optionalChatUser.isPresent()) {
            ChatUser chatUser = optionalChatUser.get();
            if (chatUser.getWelcomeMessageId() != null) {
                DeleteMessage deleteWelcomeMessage = DeleteMessage.builder()
                        .chatId(message.getChatId())
                        .messageId(chatUser.getWelcomeMessageId())
                        .build();
                bot.silent().execute(deleteWelcomeMessage);
            }
        }
    }

    @Override
    public ProcessorType getProcessorType() {
        return ProcessorType.DELETE_ADD_LINKEDIN_MESSAGE;
    }
}
