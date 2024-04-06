package com.deft.rss.service.impl;

import com.deft.rss.data.dto.RssItemDto;
import com.deft.rss.repositiory.ChatRssSettingsRepository;
import com.deft.rss.service.ChatRssAdvertService;
import com.deft.rss.service.ChatRssFeedService;
import com.deft.rss.service.ChatRssSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;

/**
 * @author Sergey Golitsyn
 * created on 30.03.2024
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRssSettingsServiceImpl implements ChatRssSettingsService {

    private final ChatRssFeedService chatRssFeedService;
    private final ChatRssSettingsRepository chatRssSettingsRepository;
    private final ChatRssAdvertService chatRssAdvertService;

    /**
     * Retrieves RSS feeds for each chat based on the chat's settings.
     * Returns a map where the key is the chat ID and the value is the concatenated RSS feed messages for that chat.
     * The RSS feeds are retrieved from the chat's RSS links and filtered by match words.
     * <p>
     * Add some adverts to chat feed
     * Add shuffle results
     *
     * @return A map of chat IDs and their corresponding RSS feed messages
     */

    @Override
    public Map<Long, String> getRssFeedToChats() {
        Map<Long, String> result = new ConcurrentHashMap<>();
        StreamSupport.stream(chatRssSettingsRepository.findAll().spliterator(), true)
                .forEach(chatRssSettings -> {
                    List<RssItemDto> rssFeed = chatRssFeedService
                            .getRssFeed(chatRssSettings.rssLinsSet(), chatRssSettings.matchWordsSet());
                    Long chatId = chatRssSettings.getChatId();

                    /*
                    Create modifiable list
                     */
                    List<RssItemDto> rssFeedModifiable = new ArrayList<>(rssFeed);

                    /*
                    Check adverts for chat
                    retrieve only one per time
                     */

                    List<RssItemDto> rssAdvertsForChat = chatRssAdvertService.getRssAdvertsForChat(chatId);
                    if (!rssAdvertsForChat.isEmpty()) {
                        rssFeedModifiable.addAll(rssAdvertsForChat);
                    }
                    /*
                    Add shuffle to mix results
                     */
                    Collections.shuffle(rssFeedModifiable);
                    String rssMessage = convertToRssMessage(rssFeedModifiable);
                    result.put(chatId, result.getOrDefault(chatId, "") + rssMessage);
                });
        return result;
    }

    private String convertToRssMessage(List<RssItemDto> rssFeed) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rssFeed.size(); i++) {
            RssItemDto rssItemDto = rssFeed.get(i);
            sb.append("\n");
            sb.append(i + 1);
            sb.append(".");
            sb.append(" ");
            sb.append(rssItemDto.convertToPrintString(150));
        }
        return sb.toString();
    }
}
