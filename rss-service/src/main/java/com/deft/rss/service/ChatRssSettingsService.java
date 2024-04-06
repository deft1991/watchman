package com.deft.rss.service;

import java.util.Map;

/**
 * ChatRssSettingsService is an interface that defines the contract for a service that handles RSS settings for chats.
 */
public interface ChatRssSettingsService {


    Map<Long, String> getRssFeedToChats();

}
