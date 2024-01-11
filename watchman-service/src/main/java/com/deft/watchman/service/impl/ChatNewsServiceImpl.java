package com.deft.watchman.service.impl;

import com.deft.watchman.data.entity.postgres.ChatNews;
import com.deft.watchman.repository.postgres.ChatNewsRepository;
import com.deft.watchman.service.ChatNewsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sergey Golitsyn
 * created on 11.01.2024
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatNewsServiceImpl implements ChatNewsService {

    public static final String HASHTAG_NEWS = "#news";
    @Value("${telegram.bot.news.pattern}")
    private String textPattern;

    private final ChatNewsRepository chatNewsRepository;

    @Override
    public boolean containsNews(String message) {
        return ParserServiceHelper.isValidInput(message, textPattern);
    }

    @Async
    @Override
    public void addNews(@NonNull String message, @NonNull Long chatId) {
        message = message.replaceAll(HASHTAG_NEWS, "");
        ChatNews cn = new ChatNews(chatId, message);

        chatNewsRepository.save(cn);
    }

    @Override
    public Map<Long, List<String>> getNews() {
        Instant monday = TimeHelper.getStartOfWeekInstant();
        List<ChatNews> news = chatNewsRepository.findAllByCreateDateIsAfter(monday);

        return news.stream().collect(Collectors.groupingBy(ChatNews::getChatId,
                Collectors.mapping(ChatNews::getNewsText, Collectors.toList())));
    }

}
