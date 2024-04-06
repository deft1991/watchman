package com.deft.rss.service.impl;

import com.deft.rss.data.dto.RssItemDto;
import com.deft.rss.data.entity.ChatRssAdvert;
import com.deft.rss.mapper.RssItemMapper;
import com.deft.rss.repositiory.ChatRssAdvertRepository;
import com.deft.rss.service.ChatRssAdvertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * @author Sergey Golitsyn
 * created on 05.04.2024
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatChatRssAdvertServiceImpl implements ChatRssAdvertService {

    @Value("${rss.feed.advert.size:1}")
    private int rssAdvertSize;

    private final RssItemMapper rssItemMapper;
    private final ChatRssAdvertRepository chatRssAdvertRepository;

    @Override
    public @NonNull List<RssItemDto> getRssAdvertsForChat(@NonNull Long chatId) {
        List<ChatRssAdvert> chatRssAdverts = chatRssAdvertRepository
                .findByChatIdAndPublishedFalseAndScheduledDateBefore(
                        chatId, Instant.now(), PageRequest.of(0, rssAdvertSize))
                .getContent();
        if (!chatRssAdverts.isEmpty()) {
            chatRssAdverts.parallelStream().forEach(el -> el.setPublished(true));
            chatRssAdvertRepository.saveAll(chatRssAdverts);
            return rssItemMapper.mapAdvertToDto(chatRssAdverts);
        }
        return Collections.emptyList();
    }
}
