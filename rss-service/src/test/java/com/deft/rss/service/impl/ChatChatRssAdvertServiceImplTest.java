package com.deft.rss.service.impl;

import com.deft.rss.data.dto.RssItemDto;
import com.deft.rss.data.entity.ChatRssAdvert;
import com.deft.rss.mapper.RssItemMapper;
import com.deft.rss.repositiory.ChatRssAdvertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.*;

//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//@TestExecutionListeners(MockitoTestExecutionListener.class)
class ChatChatRssAdvertServiceImplTest {

    @Mock
    private RssItemMapper rssItemMapper;

    @Mock
    private ChatRssAdvertRepository chatRssAdvertRepository;

    @InjectMocks
    private ChatChatRssAdvertServiceImpl chatRssAdvertServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(chatRssAdvertServiceImpl, "rssAdvertSize", 1);
    }


    @Test
    void testGetRssAdvertsForChat_NoResults() {
        Long chatId = 1L;
        given(this.chatRssAdvertRepository.findByChatIdAndPublishedFalseAndScheduledDateBefore(
                anyLong(), any(Instant.class), any(PageRequest.class))).willReturn(Page.empty());

        List<RssItemDto> rssItemDtos = chatRssAdvertServiceImpl.getRssAdvertsForChat(chatId);

        then(this.chatRssAdvertRepository).should()
                .findByChatIdAndPublishedFalseAndScheduledDateBefore(anyLong(), any(Instant.class), any(PageRequest.class));
        then(this.rssItemMapper).shouldHaveNoInteractions();

        assertTrue(rssItemDtos.isEmpty());
    }

    @Test
    void testGetRssAdvertsForChat_WithResults() {
        Long chatId = 1L;
        List<ChatRssAdvert> adverts = Collections.singletonList(new ChatRssAdvert());
        RssItemDto itemDto = new RssItemDto();
        itemDto.setTitle("title");
        itemDto.setLink("link");
        itemDto.setDescription("description");
        List<RssItemDto> expected = Collections.singletonList(itemDto);

        given(this.chatRssAdvertRepository.findByChatIdAndPublishedFalseAndScheduledDateBefore(
                anyLong(), any(Instant.class), any(PageRequest.class))).willReturn(new PageImpl<>(adverts));
        given(this.rssItemMapper.mapAdvertToDto(adverts)).willReturn(expected);

        List<RssItemDto> rssItemDtos = chatRssAdvertServiceImpl.getRssAdvertsForChat(chatId);

        then(this.chatRssAdvertRepository).should()
                .findByChatIdAndPublishedFalseAndScheduledDateBefore(anyLong(), any(Instant.class), any(PageRequest.class));
        then(this.rssItemMapper).should().mapAdvertToDto(adverts);

        assertEquals(expected, rssItemDtos);
    }
}
