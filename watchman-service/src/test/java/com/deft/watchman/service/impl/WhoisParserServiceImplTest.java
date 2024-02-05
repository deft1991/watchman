package com.deft.watchman.service.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Sergey Golitsyn
 * created on 05.02.2024
 */
@SpringBootTest(classes = {WhoisParserServiceImpl.class})
public class WhoisParserServiceImplTest {

    @Autowired
    private WhoisParserServiceImpl whoisParserService;

    @ParameterizedTest
    @CsvSource({
            "This is a valid #whois message,true",
            "#whois This is a valid message,true",
            "This is a valid message #whois,true",
            "This is not a valid message,false"
    })
    void containsSuggestTopic_ValidInput_ReturnsTrue(String message, boolean isContains) {
        // Given

        // Mocking behavior

        // When
        boolean result = whoisParserService.containsValidTag(message);

        // Then
        assertEquals(isContains, result);
    }
}
