package com.deft.watchman.processor.impl;

import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.processor.ChatUpdateProcessor;
import com.deft.watchman.processor.ProcessorType;
import com.deft.watchman.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.abilitybots.api.bot.AbilityBot;
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
@Transactional
@RequiredArgsConstructor
public class DeleteInviteMessageProcessor implements ChatUpdateProcessor {

    private final ChatUserService chatUserService;

    /**
     * Delete invite message
     * todo maybe add invite message id
     */
    @Override
    public void processUpdate(AbilityBot bot, Update update) {
        Message message = update.getMessage();
        Chat chat = message.getChat();
        Long chatId = chat.getId();
        User fromUser = message.getFrom();
        Long userId = fromUser.getId();

        Optional<ChatUser> optionalChatUser = chatUserService.findByUserIdAndChatId(userId, chatId);
        if (optionalChatUser.isPresent()) {
            ChatUser chatUser = optionalChatUser.get();
            chatUser.setInviteMessage(null);
        }
    }

    @Override
    public ProcessorType getProcessorType() {
        return ProcessorType.DELETE_INVITE_MESSAGE;
    }
}
