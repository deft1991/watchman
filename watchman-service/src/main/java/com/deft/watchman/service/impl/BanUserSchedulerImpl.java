package com.deft.watchman.service.impl;

import com.deft.watchman.bot.WatchmanBot;
import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.repository.postgres.ChatUserRepository;
import com.deft.watchman.service.BanUserScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.util.Set;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */

@Slf4j
@Service
@EnableAsync
@Transactional
@RequiredArgsConstructor
public class BanUserSchedulerImpl implements BanUserScheduler {

    private final ChatUserRepository chatUserRepository;
    private final WatchmanBot watchmanBot;

    @Value("${telegram.bot.ban-seconds:120}")
    private int banSeconds;

    @Override
    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 10)
    public void banUser() {
        Set<ChatUser> allByNewUserTrue = chatUserRepository.findAllByNewUserTrueAndLeaveFalse();
        for (ChatUser chatUser : allByNewUserTrue) {
            if (chatUser.getJoinGroupTime().isBefore(Instant.now())) {

                chatUser.setLeave(true);

                BanChatMember kickChatMember = BanChatMember.builder()
                        .chatId(chatUser.getChatId())
                        .userId(chatUser.getUserId())
                        .untilDate((int) (System.currentTimeMillis() / 1000) + banSeconds)
                        .build();

                // todo save banned users in DB
                DeleteMessage deleteMessage = DeleteMessage.builder()
                        .chatId(chatUser.getChatId())
                        .messageId(chatUser.getWelcomeMessageId())
                        .build();
                try {
                    watchmanBot.execute(deleteMessage);
                } catch (TelegramApiException e) {
                    log.error("Err: {}", e.getMessage());
                }
                try {
                    watchmanBot.execute(kickChatMember);
                } catch (TelegramApiException e) {
                    log.error("Err: {}", e.getMessage());
                }
            }
        }
    }
}
