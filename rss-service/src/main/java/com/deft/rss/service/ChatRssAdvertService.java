package com.deft.rss.service;

import com.deft.rss.data.dto.RssItemDto;
import org.springframework.lang.NonNull;

import java.util.List;


public interface ChatRssAdvertService {


    /**
     * Get RSS adverts for a chat
     *
     * @param chatId The ID of the chat
     * @return A list of RSS item DTOs representing the adverts
     */
    @NonNull
    List<RssItemDto> getRssAdvertsForChat(@NonNull Long chatId);
}
