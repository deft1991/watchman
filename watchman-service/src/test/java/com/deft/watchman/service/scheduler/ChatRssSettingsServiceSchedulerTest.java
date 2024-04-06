package com.deft.watchman.service.scheduler;

import com.deft.rss.service.ChatRssSettingsService;
import com.deft.watchman.bot.WatchmanBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

class ChatRssSettingsServiceSchedulerTest {
    @Mock
    private ChatRssSettingsService chatRssSettingsService;

    @Mock
    private WatchmanBot watchmanBot;

    @Mock
    private SilentSender silentSender;

    @InjectMocks
    private ChatRssSettingsServiceScheduler chatRssSettingsServiceScheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowNews() {
        Map<Long, String> rssFeedToChats = Collections.singletonMap(1L, "test");
        given(chatRssSettingsService.getRssFeedToChats()).willReturn(rssFeedToChats);
        doReturn(silentSender).when(watchmanBot).silent();

        chatRssSettingsServiceScheduler.showNews();

        String expectedMessageText = "#rss\\_news от Сурового Вахтера " +
                LocalDate.now() +
                " ➡️➡️" +
                "\n" +
                rssFeedToChats.get(1L);

        then(chatRssSettingsService).should().getRssFeedToChats();
        then(watchmanBot).should().silent();

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(watchmanBot.silent()).execute(messageCaptor.capture());

        SendMessage sentMessage = messageCaptor.getValue();
        assertEquals(sentMessage.getChatId(), String.valueOf(1L));
        assertEquals(sentMessage.getText(), expectedMessageText);
        String parseMode = sentMessage.getParseMode();
        assertEquals("Markdown", parseMode);
    }
}
