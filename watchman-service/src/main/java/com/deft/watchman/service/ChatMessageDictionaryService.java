package com.deft.watchman.service;

import com.deft.watchman.data.entity.postgres.ChatMessageDictionary;
import com.deft.watchman.data.entity.postgres.LanguageType;
import com.deft.watchman.data.entity.postgres.MessageType;

import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 10.02.2024
 */
public interface ChatMessageDictionaryService {

    /**
     * Get Chat Message Dictionary
     */
    Optional<ChatMessageDictionary> findByChatIdAndTypeAndLanguage(Long chatId, MessageType type, LanguageType languageType);
}
