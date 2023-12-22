package com.deft.watchman.service;

import com.deft.watchman.data.entity.postgres.ChatUser;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */
public interface ChatUserService {

    Optional<ChatUser> findByUserIdAndChatId(Long userId, Long chatId);

    Optional<ChatUser> findByUserNameAndChatId(String userName, long chatId);
    ChatUser save(ChatUser chatUser);

    ChatUser createOldUser(User user, Long chatId);

    void increaseMessageCount(Long userId, Long chatId);

    void increaseReplyToCount(Long userId, Long chatId);

    void increaseReplyFromCount(Long userId, Long chatId);
}
