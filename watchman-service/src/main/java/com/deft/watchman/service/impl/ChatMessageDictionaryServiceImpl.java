package com.deft.watchman.service.impl;

import com.deft.watchman.data.entity.postgres.ChatMessageDictionary;
import com.deft.watchman.data.entity.postgres.LanguageType;
import com.deft.watchman.data.entity.postgres.MessageDictionary;
import com.deft.watchman.data.entity.postgres.MessageType;
import com.deft.watchman.mapper.ChatMessageDictionaryMapper;
import com.deft.watchman.repository.postgres.ChatMessageDictionaryRepository;
import com.deft.watchman.repository.postgres.MessageDictionaryRepository;
import com.deft.watchman.service.ChatMessageDictionaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 10.02.2024
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageDictionaryServiceImpl implements ChatMessageDictionaryService {

    private final ChatMessageDictionaryRepository chatMessageDictionaryRepository;
    private final MessageDictionaryRepository messageDictionaryRepository;
    private final ChatMessageDictionaryMapper chatMessageDictionaryMapper;

    private List<ChatMessageDictionary> getOrCreateChatMessageDictionary(long chatId) {
        List<ChatMessageDictionary> chatMessageDictionary = chatMessageDictionaryRepository.findByChatId(chatId);
        if (chatMessageDictionary == null || chatMessageDictionary.isEmpty()) {
            Iterable<MessageDictionary> all = messageDictionaryRepository.findAll();
            chatMessageDictionary = chatMessageDictionaryMapper.mapToEntity(all, chatId);
            chatMessageDictionaryRepository.saveAll(chatMessageDictionary);
        }
        return chatMessageDictionary;
    }

    @Override
    public Optional<ChatMessageDictionary> findByChatIdAndTypeAndLanguage(Long chatId, MessageType type, LanguageType languageType) {
        Optional<ChatMessageDictionary> byChatIdAndTypeAndLanguage = chatMessageDictionaryRepository
                .findByChatIdAndTypeAndLanguage(chatId, type, languageType);
        if (byChatIdAndTypeAndLanguage.isEmpty()) {
            getOrCreateChatMessageDictionary(chatId);
        }
        return chatMessageDictionaryRepository
                .findByChatIdAndTypeAndLanguage(chatId, type, languageType);
    }
}
