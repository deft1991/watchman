package com.deft.rss.service.impl;

import com.apptasticsoftware.rssreader.DateTime;
import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;
import com.deft.rss.data.dto.RssItemDto;
import com.deft.rss.mapper.RssItemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ChatRssFeedServiceImplTest {

    public static final int RSS_FEED_SIZE = 10;
    public static final int RSS_FEED_POSTED_DAYS_AGO = 7;
    @InjectMocks
    private ChatRssFeedServiceImpl rssFeedService;

    @Mock
    private RssItemMapper rssItemMapper;

    @Mock
    private RssReader rssReader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(rssFeedService, "rssFeedSize", RSS_FEED_SIZE);
        ReflectionTestUtils.setField(rssFeedService, "rssDaysPostedAgo", RSS_FEED_POSTED_DAYS_AGO);
    }

    @Test
    void testGetRssFeed_success() {
        // Setup
        Set<String> urls = new HashSet<>(List.of("https://example.rssfeed.com/feed.xml"));
        Set<String> matchWords = new HashSet<>(List.of("title", "description"));

        Item item = new Item(new DateTime());
        item.setTitle("title");
        item.setDescription("description");
        item.setLink("link");
        item.setAuthor("author");
        item.setPubDate(LocalDate.now().toString());
        when(rssReader.read(urls)).thenReturn(Stream.of(item));

        RssItemDto rssItemDto = new RssItemDto();
        rssItemDto.setTitle("title");
        rssItemDto.setDescription("description");
        rssItemDto.setLink("link");
        when(rssItemMapper.mapToDto(any())).thenReturn(rssItemDto);

        // Run the test
        List<RssItemDto> result = rssFeedService.getRssFeed(urls, matchWords);

        // Verify the results
        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.getFirst().getTitle()).isEqualTo(rssItemDto.getTitle());
        assertThat(result.getFirst().getDescription()).isEqualTo(rssItemDto.getDescription());
        assertThat(result.getFirst().getLink()).isEqualTo(rssItemDto.getLink());

        verify(rssReader, times(1)).read(urls);
        verify(rssItemMapper, times(1)).mapToDto(item);
    }

    @Test
    void testGetRssFeed_no_match_words() {
        // Setup
        Set<String> urls = new HashSet<>(List.of("https://example.rssfeed.com/feed.xml"));
        Set<String> matchWords = new HashSet<>(List.of("word1", "word2"));

        Item item = new Item(new DateTime());
        item.setTitle("title");
        item.setDescription("description");
        item.setLink("link");
        item.setAuthor("author");
        item.setPubDate(LocalDate.now().toString());

        when(rssReader.read(urls)).thenReturn(Stream.of(item));
        RssItemDto rssItemDto = new RssItemDto();
        rssItemDto.setTitle("title");
        rssItemDto.setDescription("description");
        rssItemDto.setLink("link");
        when(rssItemMapper.mapToDto(any())).thenReturn(rssItemDto);

        // Run the test
        List<RssItemDto> result = rssFeedService.getRssFeed(urls, matchWords);

        // Verify the results
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void testGetRssFeed_empty_match_words() {
        // Setup
        Set<String> urls = new HashSet<>(List.of("https://example.rssfeed.com/feed.xml"));
        Set<String> matchWords = new HashSet<>();

        Item item = new Item(new DateTime());
        item.setTitle("title");
        item.setDescription("description");
        item.setLink("link");
        item.setAuthor("author");
        item.setPubDate(LocalDate.now().toString());

        when(rssReader.read(urls)).thenReturn(Stream.of(item));
        RssItemDto rssItemDto = new RssItemDto();
        rssItemDto.setTitle("title");
        rssItemDto.setDescription("description");
        rssItemDto.setLink("link");
        when(rssItemMapper.mapToDto(any())).thenReturn(rssItemDto);

        // Run the test
        List<RssItemDto> result = rssFeedService.getRssFeed(urls, matchWords);

        // Verify the results
        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.getFirst().getTitle()).isEqualTo(rssItemDto.getTitle());
        assertThat(result.getFirst().getDescription()).isEqualTo(rssItemDto.getDescription());
        assertThat(result.getFirst().getLink()).isEqualTo(rssItemDto.getLink());

        verify(rssReader, times(1)).read(urls);
        verify(rssItemMapper, times(1)).mapToDto(item);
    }

    @Test
    void testGetRssFeed_dates_before_rss_feed_posted_days_ago() {
        // Setup
        Set<String> urls = new HashSet<>(List.of("https://example.rssfeed.com/feed.xml"));
        Set<String> matchWords = new HashSet<>();

        Item itemOld = new Item(new DateTime());
        itemOld.setTitle("title");
        itemOld.setDescription("description");
        itemOld.setLink("link");
        itemOld.setAuthor("author");
        itemOld.setPubDate(LocalDate.now().minusDays(10).toString());

        Item item = new Item(new DateTime());
        item.setTitle("title");
        item.setDescription("description");
        item.setLink("link");
        item.setAuthor("author");
        item.setPubDate(LocalDate.now().minusDays(5).toString());

        when(rssReader.read(urls)).thenReturn(Stream.of(itemOld, item));
        RssItemDto rssItemDto = new RssItemDto();
        rssItemDto.setTitle("title");
        rssItemDto.setDescription("description");
        rssItemDto.setLink("link");
        when(rssItemMapper.mapToDto(any())).thenReturn(rssItemDto);

        // Run the test
        List<RssItemDto> result = rssFeedService.getRssFeed(urls, matchWords);

        // Verify the results
        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.getFirst().getTitle()).isEqualTo(rssItemDto.getTitle());
        assertThat(result.getFirst().getDescription()).isEqualTo(rssItemDto.getDescription());
        assertThat(result.getFirst().getLink()).isEqualTo(rssItemDto.getLink());

        verify(rssReader, times(1)).read(urls);
        verify(rssItemMapper, times(1)).mapToDto(item);
    }
}
