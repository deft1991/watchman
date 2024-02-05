package com.deft.watchman.service.impl;

import com.deft.watchman.repository.postgres.SuggestTopicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Sergey Golitsyn
 * created on 05.02.2024
 */
@SpringBootTest(classes = {SuggestTopicRepository.class, SuggestTopicServiceImpl.class})
public class SuggestTopicServiceImplTest {

    @MockBean
    private SuggestTopicRepository suggestTopicRepository;

    @Autowired
    private SuggestTopicServiceImpl suggestTopicService;

    @ParameterizedTest
    @CsvSource({
            "This is a valid #suggest_topic message,true",
            "#suggest_topic This is a valid message,true",
            "This is a valid message #suggest_topic,true",
            "This is not a valid message,false"
    })
    void containsSuggestTopic_ValidInput_ReturnsTrue(String message, boolean isContains) {
        // Given

        // Mocking behavior

        // When
        boolean result = suggestTopicService.containsSuggestTopic(message);

        // Then
        assertEquals(isContains, result);
    }

    @Test
    void addSuggestTopic_ValidInput_SavesTopicToRepository() {
        // Given
        String message = "This is a valid #suggest_topic message";
        Long chatId = 123L;

        // Mocking behavior
        when(suggestTopicRepository.save(any())).thenReturn(null); // or mock the return value as needed

        // When
        suggestTopicService.addSuggestTopic(message, chatId);

        // Then
        verify(suggestTopicRepository, times(1)).save(any());
    }
}
