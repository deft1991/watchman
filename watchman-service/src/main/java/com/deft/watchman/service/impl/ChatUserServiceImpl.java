package com.deft.watchman.service.impl;

import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.mapper.ChatUserMapper;
import com.deft.watchman.repository.postgres.ChatUserRepository;
import com.deft.watchman.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;

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
    private final ChatUserMapper chatUserMapper;

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

    @Override
    public ChatUser createOldUser(User user, Long chatId) {
        ChatUser chatUser = chatUserMapper.mapToEntity(user);
        chatUser.setNewUser(false);
        chatUser.setLeave(false);
        chatUser.setChatId(chatId);
        return chatUserRepository.save(chatUser);
    }


    @Override
    public void increaseMessageCount(Long userId, Long chatId) {
        Optional<ChatUser> optionalChatUser = chatUserRepository.findByUserIdAndChatId(userId, chatId);
        if (optionalChatUser.isEmpty()) {
            log.warn("User not found with id: {} and chatId: {}", userId, chatId);
            throw new RuntimeException("User not found");
        }
        ChatUser chatUser = optionalChatUser.get();
        chatUser.setMessageCount(chatUser.getMessageCount() + 1);
    }

    @Override
    public void increaseReplyToCount(Long userId, Long chatId) {
        Optional<ChatUser> optionalChatUser = chatUserRepository.findByUserIdAndChatId(userId, chatId);
        if (optionalChatUser.isEmpty()) {
            log.warn("User not found with id: {} and chatId: {}", userId, chatId);
            throw new RuntimeException("User not found");
        }
        ChatUser chatUser = optionalChatUser.get();
        chatUser.setReplyToCount(chatUser.getReplyToCount() + 1);
    }

    @Override
    public void increaseReplyFromCount(Long userId, Long chatId) {
        Optional<ChatUser> optionalChatUser = chatUserRepository.findByUserIdAndChatId(userId, chatId);
        if (optionalChatUser.isEmpty()) {
            log.warn("User not found with id: {} and chatId: {}", userId, chatId);
            throw new RuntimeException("User not found");
        }
        ChatUser chatUser = optionalChatUser.get();
        chatUser.setReplyFromCount(chatUser.getReplyFromCount() + 1);
    }
}
