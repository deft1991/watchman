package com.deft.watchman.processor.impl;

import com.deft.watchman.data.entity.postgres.ChatMessageDictionary;
import com.deft.watchman.data.entity.postgres.LanguageType;
import com.deft.watchman.data.entity.postgres.MessageDictionary;
import com.deft.watchman.data.entity.postgres.MessageType;
import com.deft.watchman.processor.ChatUpdateProcessor;
import com.deft.watchman.repository.postgres.MessageDictionaryRepository;
import com.deft.watchman.service.ChatMessageDictionaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 10.02.2024
 */

@Slf4j
@RequiredArgsConstructor
public abstract class BaseDictionaryChatUpdateProcessor implements ChatUpdateProcessor {

    private final MessageDictionaryRepository messageDictionaryRepository;
    private final ChatMessageDictionaryService chatMessageDictionaryService;

    public ChatMessageDictionary getMessageDictionary(
            long chatId,
            MessageType type,
            LanguageType languageType,
            String defaultMessage) {

        Optional<ChatMessageDictionary> cmd = chatMessageDictionaryService
                .findByChatIdAndTypeAndLanguage(chatId, type, languageType);
        ChatMessageDictionary chatMessageDictionary = new ChatMessageDictionary();
        if (cmd.isEmpty()) {
            log.warn("ChatMessageDictionary with type {} and language {} is empty", type, languageType);
            Optional<MessageDictionary> byTypeAndLanguage = messageDictionaryRepository.findByTypeAndLanguage(type, languageType);
            if (byTypeAndLanguage.isPresent()) {
                log.warn("MessageDictionary with type {} and language {} is empty", type, languageType);
                chatMessageDictionary.setMessage(byTypeAndLanguage.get().getMessage());
            } else {
                chatMessageDictionary.setMessage(defaultMessage);
            }
        } else {
            chatMessageDictionary = cmd.get();
        }
        return chatMessageDictionary;
    }
}
