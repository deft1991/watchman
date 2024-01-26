package com.deft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication(scanBasePackages = {"com.deft.*"})
@EnableJpaRepositories(basePackages = {"com.deft.*"})
@EntityScan(basePackages = {"com.deft.*"})
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
