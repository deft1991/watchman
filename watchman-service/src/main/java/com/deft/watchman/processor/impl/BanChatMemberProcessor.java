package com.deft.watchman.processor.impl;

import com.deft.watchman.data.entity.postgres.ChatSettings;
import com.deft.watchman.processor.ChatUpdateProcessor;
import com.deft.watchman.processor.ProcessorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * @author Sergey Golitsyn
 * created on 21.12.2023
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BanChatMemberProcessor implements ChatUpdateProcessor {

    @Value("${telegram.bot.ban-seconds:120}")
    private int banSeconds;

    @Override
    public void processUpdate(AbilityBot bot, Update update, ChatSettings settings) {
        Message message = update.getMessage();
        Chat chat = message.getChat();
        User fromUser = message.getFrom();
        Long userId = fromUser.getId();
        /*
        Kick user if his first message was without #whois tag
        todo or without linked in link
        */
        BanChatMember kickChatMember = BanChatMember.builder()
                .chatId(chat.getId())
                .userId(userId)
                .untilDate((int) (System.currentTimeMillis() / 1000) + banSeconds)
                .build();
        // todo save banned users in DB

        bot.silent().execute(kickChatMember);

    }

    @Override
    public ProcessorType getProcessorType() {
        return ProcessorType.BAN_CHAT_MEMBER;
    }
}
