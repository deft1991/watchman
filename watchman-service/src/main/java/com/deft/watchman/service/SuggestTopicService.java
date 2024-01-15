package com.deft.watchman.service;

import lombok.NonNull;

/**
 * @author Sergey Golitsyn
 * created on 15.01.2024
 */
public interface SuggestTopicService {

    boolean containsSuggestTopic(String message);

    void addSuggestTopic(@NonNull String message, @NonNull Long chatId);
}
