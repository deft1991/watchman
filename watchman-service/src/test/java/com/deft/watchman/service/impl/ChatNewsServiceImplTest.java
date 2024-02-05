package com.deft.watchman.service.impl;

import com.deft.watchman.data.entity.postgres.ChatNews;
import com.deft.watchman.repository.postgres.ChatNewsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ChatNewsServiceImpl.class, ChatNewsRepository.class})
class ChatNewsServiceImplTest {

    @MockBean
    private ChatNewsRepository chatNewsRepository;

    @Autowired
    private ChatNewsServiceImpl chatNewsService;

    @ParameterizedTest
    @ValueSource(strings = {
            "This is a valid #news message",
            "#news Valid message with tag",
            "Another valid message #news"
    })
    void containsNews_ValidInput_ReturnsTrue(String message) {
        // Given

        // When
        boolean result = chatNewsService.containsNews(message);

        // Then
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "This is not a valid message",
            "Invalid message",
            "A news tag is missing",
            "Message without any tag"
    })
    void containsNews_InvalidInput_ReturnsFalse(String message) {
        // Given

        // When
        boolean result = chatNewsService.containsNews(message);

        // Then
        assertFalse(result);
    }

    @Test
    void getNews_ValidInput_ReturnsMappedChatNews() {
        // Given
        List<ChatNews> chatNewsList = new ArrayList<>();
        chatNewsList.add(new ChatNews(1L, "News 1"));
        chatNewsList.add(new ChatNews(1L, "News 1.1"));
        chatNewsList.add(new ChatNews(2L, "News 2"));
        when(chatNewsRepository.findAllByCreateDateIsAfter(any())).thenReturn(chatNewsList);

        // When
        var result = chatNewsService.getNews();

        // Then
        assertEquals(2, result.size());
        assertEquals(2, result.get(1L).size());
        assertEquals("News 1", result.get(1L).get(0));
        assertEquals("News 1.1", result.get(1L).get(1));
        assertEquals(1, result.get(2L).size());
        assertEquals("News 2", result.get(2L).getFirst());
    }
}
