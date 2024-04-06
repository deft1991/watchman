package com.deft.rss.service.impl;

import com.apptasticsoftware.rssreader.RssReader;
import com.apptasticsoftware.rssreader.util.ItemComparator;
import com.deft.rss.data.dto.RssItemDto;
import com.deft.rss.mapper.RssItemMapper;
import com.deft.rss.service.ChatRssFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Sergey Golitsyn
 * created on 30.03.2024
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRssFeedServiceImpl implements ChatRssFeedService {

    @Value("${rss.feed.size:5}")
    private long rssFeedSize;

    @Value("${rss.feed.posted.days.ago:7}")
    private long rssDaysPostedAgo;

    private final RssItemMapper rssItemMapper;
    private final RssReader rssReader;

    public List<RssItemDto> getRssFeed(@NonNull Set<String> urls, Set<String> matchWords) {
        return rssReader.read(urls)
                .parallel()
                .filter(item -> {
                    Optional<ZonedDateTime> pubDateZonedDateTime = item.getPubDateZonedDateTime();
                    return pubDateZonedDateTime.map(date -> date.isAfter(
                                    ZonedDateTime.now().minusDays(rssDaysPostedAgo)))
                            .orElse(false);
                })
                .filter(item -> {
                    Optional<String> value = item.getTitle();
                    return isContainsWordOrFalse(matchWords, value);
                })
                .filter(item -> {
                    Optional<String> value = item.getDescription();
                    return isContainsWordOrFalse(matchWords, value);
                })
                .sorted(ItemComparator.oldestItemFirst())
                .map(rssItemMapper::mapToDto)
                .limit(rssFeedSize)
                .toList();
    }

    private static boolean isContainsWordOrFalse(Set<String> matchWords, Optional<String> value) {
        if (matchWords.isEmpty()) {
            return true;
        }
        return matchWords.parallelStream()
                .anyMatch(word -> value.map(t -> t.contains(word)).orElse(false));
    }
}
