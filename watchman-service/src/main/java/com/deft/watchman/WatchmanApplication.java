package com.deft.watchman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class WatchmanApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(WatchmanApplication.class, args);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(ctx.getBean("pizzaBot", AbilityBot.class));
        } catch (TelegramApiException ex) {
            throw new RuntimeException(ex);
        }
    }
}
