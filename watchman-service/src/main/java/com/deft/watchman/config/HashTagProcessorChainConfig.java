package com.deft.watchman.config;

import com.deft.watchman.processor.hashtag.ChatNewsHashTagProcessorChain;
import com.deft.watchman.processor.hashtag.HashTagProcessorChain;
import com.deft.watchman.processor.hashtag.SuggestionTopicProcessorChain;
import com.deft.watchman.service.ChatNewsService;
import com.deft.watchman.service.SuggestTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sergey Golitsyn
 * created on 15.01.2024
 */

@Configuration
@RequiredArgsConstructor
public class HashTagProcessorChainConfig {

    @Bean
    public HashTagProcessorChain hashTagProcessorChain(ChatNewsService chatNewsService, SuggestTopicService suggestTopicService) {
        HashTagProcessorChain chat = new ChatNewsHashTagProcessorChain(chatNewsService);
        HashTagProcessorChain suggestion = new SuggestionTopicProcessorChain(suggestTopicService);
        return HashTagProcessorChain.link(chat, suggestion);
    }
}
