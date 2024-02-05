package com.deft.watchman.service.scheduler;

import com.deft.watchman.bot.WatchmanBot;
import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.repository.postgres.ChatUserRepository;
import com.deft.watchman.service.BanUserScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

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
@ConditionalOnProperty(
        value = "app.scheduling.enable", havingValue = "true", matchIfMissing = true
)
public class BanUserSchedulerImpl implements BanUserScheduler {

    private final ChatUserRepository chatUserRepository;
    private final WatchmanBot watchmanBot;

    @Value("${telegram.bot.ban-seconds:120}")
    private int banSeconds;

    @Value("${scheduler.ban.wait-time-seconds:300}")
    private int banWaitTimeSeconds;

    @Override
    @Async
    @Scheduled(fixedDelayString = "${scheduler.ban.fixed-rate.in.milliseconds:600_000}")
    public void banUser() {
        Set<ChatUser> allByNewUserTrue = chatUserRepository.findUsersForBan(banWaitTimeSeconds);
        for (ChatUser chatUser : allByNewUserTrue) {
            chatUser.setLeave(true);

            BanChatMember kickChatMember = BanChatMember
                    .builder()
                    .chatId(chatUser.getChatId())
                    .userId(chatUser.getUserId())
                    .untilDate((int) (System.currentTimeMillis() / 1000) + banSeconds)
                    .build();

            // todo maybe save banned users in DB
            if (chatUser.getWelcomeMessageId() != null) {
                DeleteMessage deleteMessage = DeleteMessage
                        .builder()
                        .chatId(chatUser.getChatId())
                        .messageId(chatUser.getWelcomeMessageId())
                        .build();
                watchmanBot.silent().execute(deleteMessage);
            }
            watchmanBot.silent().execute(kickChatMember);
        }
    }
}
