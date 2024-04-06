package com.deft.rss.service;

import com.deft.rss.data.dto.RssItemDto;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

/**
 * @author Sergey Golitsyn
 * created on 30.03.2024
 */
public interface ChatRssFeedService {

    /**
     * Get rss feed based on
     *
     * @param urls       Then filter data by
     * @param matchWords If title or description matches --> add Rss Item to result
     */
    List<RssItemDto> getRssFeed(@NonNull Set<String> urls, Set<String> matchWords);
}
