package com.deft.watchman.service;

import com.deft.watchman.data.entity.postgres.ChatUser;

import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */
public interface ChatUserService {

    Optional<ChatUser> findByUserIdAndChatId(Long userId, Long chatId);
    ChatUser save(ChatUser chatUser);
}
