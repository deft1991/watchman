package com.deft.watchman.service.impl;

import com.deft.watchman.data.entity.postgres.ChatSettings;
import com.deft.watchman.repository.postgres.ChatSettingsRepository;
import com.deft.watchman.service.ChatSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 19.01.2024
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatSettingsServiceImpl implements ChatSettingsService {

    @Value("${chat.default.language:RUS}")
    private String defaultLanguage;
    @Value("${chat.default.linkedin-enable:true}")
    private boolean linkedInEnable;

    private final ChatSettingsRepository chatSettingsRepository;


    /**
     * Get or create Chat Settings by chatId
     * If chat settings is empty --> create with default params
     * todo move default params to DB
     */
    @Override
    public ChatSettings getChatSettings(long chatId, String chatName) {
        Optional<ChatSettings> byId = chatSettingsRepository.findByChatId(chatId);
        if (byId.isEmpty()) {
            ChatSettings chatSettings = ChatSettings.builder()
                    .chatId(chatId)
                    .chatLanguage(defaultLanguage)
                    .chatName(chatName)
                    .linkedinEnable(linkedInEnable)
                    .build();
            return chatSettingsRepository.save(chatSettings);
        } else {
            return byId.get();
        }
    }
}
