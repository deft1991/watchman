package com.deft.rss.config;

import com.apptasticsoftware.rssreader.RssReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sergey Golitsyn
 * created on 30.03.2024
 */

@Configuration
public class RssConfig {

    @Bean
    public RssReader rssReader() {
        return new RssReader();
    }
}
