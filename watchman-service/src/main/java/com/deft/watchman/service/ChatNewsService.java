package com.deft.watchman.service;

import lombok.NonNull;

import java.util.List;
import java.util.Map;

/**
 * @author Sergey Golitsyn
 * created on 11.01.2024
 */
public interface ChatNewsService {

    boolean containsNews(String message);

    void addNews(@NonNull String message, @NonNull Long chatId);

    Map<Long, List<String>> getNews();
}
