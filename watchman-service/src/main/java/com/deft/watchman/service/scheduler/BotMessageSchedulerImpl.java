package com.deft.watchman.service.scheduler;

import com.deft.watchman.bot.WatchmanBot;
import com.deft.watchman.service.BotMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */

@Slf4j
@Service
@EnableAsync
@Transactional
@RequiredArgsConstructor
public class BotMessageSchedulerImpl {

    private final WatchmanBot watchmanBot;
    private final BotMessageService botMessageService;

    @Async
//    second, minute, hour, day of month, month, day(s) of week
//    To have a job run every SUNDAY at 1 AM
    @Scheduled(cron = "${scheduler.message.cron:* * * * *}")
    public void sendBotMessages() {
        List<Triple<Long, String, String>> messagesToSend = botMessageService.getMessagesToSend();

        /*
         TODO here we need to add some flexible checks and retry polices.
          For example bot could be kicked from the group.
          Or we got a network err. He have to try send message at least few times.
         */
        List<String> sent = new ArrayList<>();
        messagesToSend.parallelStream().forEach(triple -> {
            Long chatId = triple.getLeft();
            String message = triple.getMiddle();
            String messageId = triple.getRight();
            sent.add(messageId);
            SendMessage inviteMessage = SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text(message)
                    .build();
            watchmanBot.silent().execute(inviteMessage);
        });
        if (!sent.isEmpty()) {
            botMessageService.markAsSent(sent);
        }
    }
}
