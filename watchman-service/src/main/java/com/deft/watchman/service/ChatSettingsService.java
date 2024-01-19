package com.deft.watchman.service;

import com.deft.watchman.data.entity.postgres.ChatSettings;

/**
 * @author Sergey Golitsyn
 * created on 19.01.2024
 */
public interface ChatSettingsService {

    /**
     * Get or create Chat Settings by chatId
     */
    ChatSettings getChatSettings(long chatId, String chatName);
}
