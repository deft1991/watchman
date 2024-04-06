package com.deft.watchman.service.scheduler;

import com.deft.rss.service.ChatRssSettingsService;
import com.deft.watchman.bot.WatchmanBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.util.Map;

/**
 * @author Sergey Golitsyn
 * created on 30.03.2024
 */

@Slf4j
@Service
@EnableAsync
@Transactional
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "app.scheduling.enable", havingValue = "true", matchIfMissing = true
)
public class ChatRssSettingsServiceScheduler {

    private final ChatRssSettingsService chatRssSettingsService;
    private final WatchmanBot watchmanBot;

    /**
     * executed every 2 days --> 0 0 0 *\\/2 * ?
     */
    @Async
    @Scheduled(cron = "${scheduler.rss.cron:0 0 0 */2 * ?}")
//    @Scheduled(cron = "*/30 * * * * *")
    public void showNews() {
        Map<Long, String> rssFeedToChats = chatRssSettingsService.getRssFeedToChats();

        rssFeedToChats.entrySet().parallelStream().forEach(entry -> {
            Long chatId = entry.getKey();
            String sb = "#rss\\_news от Сурового Вахтера " +
                    LocalDate.now() +
                    " ➡️➡️" +
                    "\n" +
                    entry.getValue();

            SendMessage message = SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text(sb)
                    .build();
            message.enableMarkdown(true);
            watchmanBot.silent().execute(message);
        });
    }
}
