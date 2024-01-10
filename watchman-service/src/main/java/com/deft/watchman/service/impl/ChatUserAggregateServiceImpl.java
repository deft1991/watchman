package com.deft.watchman.service.impl;

import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.repository.postgres.ChatUserRepository;
import com.deft.watchman.service.ChatUserAggregatedService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatUserAggregateServiceImpl implements ChatUserAggregatedService {

    private final ChatUserRepository chatUserRepository;

    @Override
    public List<String> getTopSpeakers(@NonNull Long chatId) {
        return chatUserRepository.findTop5ByChatIdOrderByMessageCountDesc(chatId)
                .stream()
                .map(ChatUser::getUserName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getTopReplyTo(@NonNull Long chatId) {
        return chatUserRepository.findTop5ByChatIdOrderByReplyToCountDesc(chatId)
                .stream()
                .map(ChatUser::getUserName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getTopReplyFrom(@NonNull Long chatId) {
        return chatUserRepository.findTop5ByChatIdOrderByReplyFromCountDesc(chatId)
                .stream()
                .map(ChatUser::getUserName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getTopRating(@NonNull Long chatId) {
        return chatUserRepository.findTop5ByChatIdOrderByRatingDesc(chatId)
                .stream()
                .map(ChatUser::getUserName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getTopUser(@NonNull Long chatId) {
        return chatUserRepository.findTop5Users(chatId)
                .stream()
                .map(ChatUser::getUserName)
                .collect(Collectors.toList());
    }
}
