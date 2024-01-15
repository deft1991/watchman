package com.deft.watchman.service.impl;

import com.deft.watchman.data.entity.postgres.SuggestTopic;
import com.deft.watchman.repository.postgres.SuggestTopicRepository;
import com.deft.watchman.service.SuggestTopicService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sergey Golitsyn
 * created on 15.01.2024
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SuggestTopicServiceImpl implements SuggestTopicService {

    public static final String HASHTAG_SUGGESTION_TOPIC = "#suggest_topic";
    @Value("${telegram.bot.suggest-topic.pattern}")
    private String textPattern;

    private final SuggestTopicRepository suggestTopicRepository;

    @Override
    public boolean containsSuggestTopic(String message) {
        return ParserServiceHelper.isValidInput(message, textPattern);
    }

    @Override
    public void addSuggestTopic(@NonNull String message, @NonNull Long chatId) {
        message = message.replaceAll(HASHTAG_SUGGESTION_TOPIC, "");
        SuggestTopic suggestTopic = new SuggestTopic(chatId, message);
        log.info("Add topic: {} to chat: {}", message, chatId);
        suggestTopicRepository.save(suggestTopic);
    }
}
