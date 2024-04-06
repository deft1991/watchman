package com.deft.rss.service.impl;

import com.deft.rss.data.dto.RssItemDto;
import com.deft.rss.data.entity.ChatRssSettings;
import com.deft.rss.repositiory.ChatRssSettingsRepository;
import com.deft.rss.service.ChatRssAdvertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.deft.rss.data.dto.RssItemDto.BOLD_DESCRIPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatRssSettingsServiceImplTest {

    public static final int RSS_FEED_SIZE = 5;

    @Mock
    private ChatRssFeedServiceImpl rssFeedService;

    @Mock
    private ChatRssSettingsRepository chatRssSettingsRepository;

    @Mock
    private ChatRssAdvertService chatRssAdvertService;

    @InjectMocks
    private ChatRssSettingsServiceImpl chatRssSettingsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(rssFeedService, "rssFeedSize", RSS_FEED_SIZE);
    }

    @Test
    void testGetRssFeedToChats_success_with_empty_adverts() {
        // Setup
        ChatRssSettings chatRssSettings = new ChatRssSettings();
        chatRssSettings.setChatId(1L);
        chatRssSettings.setRssLinks(new String[]{"https://example.rssfeed.com/feed.xml"});
        chatRssSettings.setMatchWords(new String[]{"word1", "word2"});

        when(chatRssSettingsRepository.findAll()).thenReturn(List.of(chatRssSettings));
        when(chatRssAdvertService.getRssAdvertsForChat(any())).thenReturn(Collections.emptyList());

        RssItemDto rssItemDto = new RssItemDto();
        rssItemDto.setTitle("title");
        rssItemDto.setDescription("description");
        rssItemDto.setLink("link");

        List<RssItemDto> rssFeed = new ArrayList<>();
        rssFeed.add(rssItemDto);

        when(rssFeedService.getRssFeed(chatRssSettings.rssLinsSet(), chatRssSettings.matchWordsSet()))
                .thenReturn(rssFeed);

        String expected = "\n1. [title](link)\n" + BOLD_DESCRIPTION + " " + rssItemDto.getDescription() + "\n";

        // Run the test
        Map<Long, String> result = chatRssSettingsService.getRssFeedToChats();

        // Verify the results
        assertEquals(1, result.size());
        assertEquals(expected, result.get(1L));
    }

    @Test
    void testGetRssFeedToChats_success_with_adverts() {
        // Setup
        long CHAT_ID = 1L;
        ChatRssSettings chatRssSettings = new ChatRssSettings();
        chatRssSettings.setChatId(CHAT_ID);
        chatRssSettings.setRssLinks(new String[]{"https://example.rssfeed.com/feed.xml"});
        chatRssSettings.setMatchWords(new String[]{"word1", "word2"});

        RssItemDto rssItemDtoAdv = new RssItemDto();
        rssItemDtoAdv.setTitle("adv_title");
        rssItemDtoAdv.setDescription("adv_description");
        rssItemDtoAdv.setLink("adv_link");

        when(chatRssSettingsRepository.findAll()).thenReturn(List.of(chatRssSettings));
        when(chatRssAdvertService.getRssAdvertsForChat(any())).thenReturn(List.of(rssItemDtoAdv));

        RssItemDto rssItemDto = new RssItemDto();
        rssItemDto.setTitle("title");
        rssItemDto.setDescription("description");
        rssItemDto.setLink("link");

        List<RssItemDto> rssFeed = new ArrayList<>();
        rssFeed.add(rssItemDto);

        when(rssFeedService.getRssFeed(chatRssSettings.rssLinsSet(), chatRssSettings.matchWordsSet()))
                .thenReturn(rssFeed);

        String expected = "\n1. [title](link)\n" + BOLD_DESCRIPTION + " " + rssItemDto.getDescription() + "\n";

        // Run the test
        Map<Long, String> result = chatRssSettingsService.getRssFeedToChats();

        // Verify the results
        assertEquals(1, result.size());
        assertTrue(expected.length() < result.get(CHAT_ID).length());
        verify(chatRssAdvertService, times(1)).getRssAdvertsForChat(any());
    }
}
