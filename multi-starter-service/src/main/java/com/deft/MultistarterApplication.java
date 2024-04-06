package com.deft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * The main Spring Boot Application class for the Multistarter Project.
 *
 * <p>This class is responsible for configuring and starting the Spring Boot application. The SpringBootApplication
 * annotation tells Spring Boot where to do its component scan for Spring beans. It also tells Spring Boot to enable
 * Spring JPA Repositories and to scan for JPA Entities.</p>
 *
 * <p>The ComponentScan annotation is configured to exclude com.deft.watchman.WatchmanApplication from the component
 * scan by using a REGEX type filter.</p>
 *
 * <p>The main method runs the Spring Boot application and also starts a Telegram bot named watchmanBot. The bot
 * is registered through TelegramBotsApi, and is fetched from the spring context as AbilityBot.</p>
 *
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.domain.EntityScan
 * @see org.springframework.data.jpa.repository.config.EnableJpaRepositories
 * @see org.springframework.context.annotation.ComponentScan
 */

@SpringBootApplication(scanBasePackages = {"com.deft.*"})
@EnableJpaRepositories(basePackages = {"com.deft.*"})
@EntityScan(basePackages = {"com.deft.*"})
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.deft\\.watchman\\.WatchmanApplication"))
public class MultistarterApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(MultistarterApplication.class, args);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            AbilityBot watchmanBot = ctx.getBean("watchmanBot", AbilityBot.class);
            botsApi.registerBot(watchmanBot);
        } catch (TelegramApiException ex) {
            throw new RuntimeException(ex);
        }
    }
}
