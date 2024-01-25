package com.deft.watchman.service.scheduler;

import com.deft.watchman.bot.WatchmanBot;
import com.deft.watchman.service.ChatNewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */

@Slf4j
@Service
@EnableAsync
@Transactional
@RequiredArgsConstructor
public class ChatNewsSchedulerImpl {

    private final ChatNewsService chatNewsService;
    private final WatchmanBot watchmanBot;

    @Async
//    second, minute, hour, day of month, month, day(s) of week
//    To have a job run every SUNDAY at 1 AM
//    @Scheduled(cron = "${scheduler.news.cron:0 0 1 * * SUN}")
    @Scheduled(cron = "*/10 * * * * ?")
    public void showNews() {
        Map<Long, List<String>> news = chatNewsService.getNews();

        news.entrySet().parallelStream().forEach(n -> {
            Long chatId = n.getKey();
            StringBuilder sb = new StringBuilder();
            sb.append("#digest от Сурового Вахтера ");
            sb.append(LocalDate.now());
            sb.append(" ➡\uFE0F➡\uFE0F");
            sb.append("\n");
            sb.append("\n");
            List<String> value = n.getValue();
            for (int i = 0; i < value.size(); i++) {
                sb.append(i);
                sb.append(". ");
                sb.append(value.get(i));
                sb.append("\n");
            }

            SendMessage inviteMessage = SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text(sb.toString())
                    .build();
            watchmanBot.silent().execute(inviteMessage);
        });
    }
}
