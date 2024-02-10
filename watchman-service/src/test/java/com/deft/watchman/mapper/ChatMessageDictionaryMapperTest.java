package com.deft.watchman.mapper;

import com.deft.watchman.data.entity.postgres.ChatMessageDictionary;
import com.deft.watchman.data.entity.postgres.LanguageType;
import com.deft.watchman.data.entity.postgres.MessageDictionary;
import com.deft.watchman.data.entity.postgres.MessageType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChatMessageDictionaryMapperTest {

    private final ChatMessageDictionaryMapper mapper = Mappers.getMapper(ChatMessageDictionaryMapper.class);

    @Test
    void mapToEntity_SingleMessageDictionary_ReturnsChatMessageDictionary() {
        // Given
        MessageDictionary messageDictionary = MessageDictionary.builder()
                .type(MessageType.WELCOME_MESSAGE)
                .message("message")
                .language(LanguageType.ENG)
                .build();
        long chatId = 123; // Example chat ID

        // When
        ChatMessageDictionary chatMessageDictionary = mapper.mapToEntity(messageDictionary, chatId);

        // Then
        assertNotNull(chatMessageDictionary);
        assertEquals(messageDictionary.getType(), chatMessageDictionary.getType());
        assertEquals(messageDictionary.getMessage(), chatMessageDictionary.getMessage());
        assertEquals(messageDictionary.getLanguage(), chatMessageDictionary.getLanguage());
        assertEquals(chatId, chatMessageDictionary.getChatId());
    }

    @Test
    void mapToEntity_ListOfMessageDictionaries_ReturnsListOfChatMessageDictionaries() {
        // Given
        MessageDictionary messageDictionary1 = MessageDictionary.builder()
                .type(MessageType.WELCOME_MESSAGE)
                .message("message")
                .language(LanguageType.ENG)
                .build();
        MessageDictionary messageDictionary2 = MessageDictionary.builder()
                .type(MessageType.ADD_LINKEDIN_MESSAGE)
                .message("message 2")
                .language(LanguageType.RUS)
                .build();
        List<MessageDictionary> messageDictionaries = Arrays.asList(
                messageDictionary1,
                messageDictionary2
        );
        long chatId = 123; // Example chat ID

        // When
        List<ChatMessageDictionary> chatMessageDictionaries = mapper.mapToEntity(messageDictionaries, chatId);

        // Then
        assertNotNull(chatMessageDictionaries);
        assertEquals(messageDictionaries.size(), chatMessageDictionaries.size());
        assertEquals(chatId, chatMessageDictionaries.get(0).getChatId());
        assertEquals(chatId, chatMessageDictionaries.get(1).getChatId());
    }
}
