package com.deft.watchman.service.impl;

import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.repository.postgres.ChatUserRepository;
import com.deft.watchman.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatUserServiceImpl implements ChatUserService {

    private final ChatUserRepository chatUserRepository;

    @Override
    public Optional<ChatUser> findByUserIdAndChatId(Long userId, Long chatId) {
        Optional<ChatUser> byUserIdAndChatId = chatUserRepository.findByUserIdAndChatId(userId, chatId);
        if (byUserIdAndChatId.isEmpty()) {
            log.warn("User not found with id: {} and chatId: {}", userId, chatId);
        }
        return byUserIdAndChatId;
    }

    @Override
    public ChatUser save(ChatUser chatUser) {
        return chatUserRepository.save(chatUser);
    }
}
