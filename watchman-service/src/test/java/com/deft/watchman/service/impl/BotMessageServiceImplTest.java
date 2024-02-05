package com.deft.watchman.service.impl;

import com.deft.watchman.data.entity.postgres.BotMessage;
import com.deft.watchman.repository.postgres.BotMessageRepository;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BotMessageServiceImplTest {

    @Mock
    private BotMessageRepository botMessageRepository;

    @InjectMocks
    private BotMessageServiceImpl botMessageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMessagesToSend_ReturnsExpectedList() {
        // Given
        BotMessage message1 = BotMessage.builder()
                .chatId(1L)
                .message("Message 1")
                .scheduledSendTime(Instant.now().minus(10, ChronoUnit.MINUTES))
                .build();
        BotMessage message2 = BotMessage.builder()
                .chatId(2L)
                .message("Message 2")
                .scheduledSendTime(Instant.now().minus(10, ChronoUnit.MINUTES))
                .build();
        when(botMessageRepository.findAllForSend()).thenReturn(List.of(message1, message2));

        // When
        List<Triple<Long, String, String>> messagesToSend = botMessageService.getMessagesToSend();

        // Then
        assertEquals(2, messagesToSend.size());
        assertEquals(1L, messagesToSend.get(0).getLeft());
        assertEquals("Message 1", messagesToSend.get(0).getMiddle());
        assertEquals(2L, messagesToSend.get(1).getLeft());
        assertEquals("Message 2", messagesToSend.get(1).getMiddle());
    }

    @Test
    void markAsSent_CallsRepositoryMethod() {
        // Arrange
        List<String> sentMessages = List.of("1", "2", "3");

        // Act
        botMessageService.markAsSent(sentMessages);

        // Assert
        verify(botMessageRepository, times(1)).markAsSent(sentMessages);
    }
}
