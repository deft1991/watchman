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
import org.springframework.data.domain.Pageable;
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

        // FIX: Change the method name to match what the service actually calls
        given(this.chatRssAdvertRepository.getAvailableAdvertsForChat(
                anyLong(), any(Instant.class), any(Pageable.class)))
                .willReturn(Page.empty());

        // Execute
        List<RssItemDto> rssItemDtos = chatRssAdvertServiceImpl.getRssAdvertsForChat(chatId);

        // Verify
        then(this.chatRssAdvertRepository).should()
                .getAvailableAdvertsForChat(anyLong(), any(Instant.class), any(Pageable.class));

        then(this.rssItemMapper).shouldHaveNoInteractions();
        assertTrue(rssItemDtos.isEmpty());
    }

    @Test
    void testGetRssAdvertsForChat_WithResults() {
        // 1. Arrange
        Long chatId = 1L;
        ChatRssAdvert advert = new ChatRssAdvert();
        List<ChatRssAdvert> adverts = Collections.singletonList(advert);

        RssItemDto itemDto = new RssItemDto();
        itemDto.setTitle("title");
        itemDto.setLink("link");
        itemDto.setDescription("description");
        List<RssItemDto> expected = Collections.singletonList(itemDto);

        // FIX 1: Use the correct method name 'getAvailableAdvertsForChat'
        // FIX 2: Use 'any(Pageable.class)' instead of 'PageRequest.class'
        given(this.chatRssAdvertRepository.getAvailableAdvertsForChat(
                anyLong(), any(Instant.class), any(Pageable.class)))
                .willReturn(new PageImpl<>(adverts));

        given(this.rssItemMapper.mapAdvertToDto(adverts)).willReturn(expected);

        // 2. Act
        List<RssItemDto> rssItemDtos = chatRssAdvertServiceImpl.getRssAdvertsForChat(chatId);

        // 3. Assert
        // FIX 3: Verify the correct method name
        then(this.chatRssAdvertRepository).should()
                .getAvailableAdvertsForChat(anyLong(), any(Instant.class), any(Pageable.class));

        then(this.rssItemMapper).should().mapAdvertToDto(adverts);

        assertEquals(expected.size(), rssItemDtos.size());
        assertEquals(expected.getFirst().getTitle(), rssItemDtos.getFirst().getTitle());
    }

}
