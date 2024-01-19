package com.deft.watchman.processor.impl;

import com.deft.watchman.data.entity.postgres.ChatSettings;
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
public class LeaveGroupProcessor implements ChatUpdateProcessor {

    private final ChatUserService chatUserService;

    @Override
    public void processUpdate(AbilityBot bot, Update update, ChatSettings settings) {
        Message message = update.getMessage();
        Chat chat = message.getChat();
        User user = message.getFrom();

        if (chat.isGroupChat() || chat.isSuperGroupChat()) {
            Optional<ChatUser> optionalChatUser = chatUserService.findByUserIdAndChatId(user.getId(), chat.getId());
            ChatUser chatUser;
            if (optionalChatUser.isPresent()) {
                chatUser = optionalChatUser.get();
                chatUser.setLeave(true);
            }
        }
    }

    @Override
    public ProcessorType getProcessorType() {
        return ProcessorType.LEAVE_GROUP;
    }
}
