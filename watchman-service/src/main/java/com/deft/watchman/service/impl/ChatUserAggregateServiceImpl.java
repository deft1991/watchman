package com.deft.watchman.service.impl;

import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.repository.postgres.ChatUserRepository;
import com.deft.watchman.service.ChatUserAggregatedService;
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
    public List<String> getTopSpeakers() {
        return chatUserRepository.findTop5ByOrderByMessageCountDesc()
                .stream()
                .map(ChatUser::getUserName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getTopReplyTo() {
        return chatUserRepository.findTop5ByOrderByReplyToCountDesc()
                .stream()
                .map(ChatUser::getUserName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getTopReplyFrom() {
        return chatUserRepository.findTop5ByOrderByReplyFromCountDesc()
                .stream()
                .map(ChatUser::getUserName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getTopRating() {
        return chatUserRepository.findTop5ByOrderByRatingDesc()
                .stream()
                .map(ChatUser::getUserName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getTopUser() {
        return chatUserRepository.findTop5Users()
                .stream()
                .map(ChatUser::getUserName)
                .collect(Collectors.toList());
    }
}
