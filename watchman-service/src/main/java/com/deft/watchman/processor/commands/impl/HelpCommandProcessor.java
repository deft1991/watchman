package com.deft.watchman.processor.commands.impl;

import com.deft.watchman.data.entity.postgres.ChatSettings;
import com.deft.watchman.processor.commands.CommandType;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */

@Service
public class HelpCommandProcessor extends BasicCommandProcessor {


    @Override
    public void processCommand(AbilityBot bot, Update update, ChatSettings chatSettings) {
        Message message = update.getMessage();
        Chat chat = message.getChat();

        StringBuilder sb = new StringBuilder();

        Arrays.stream(CommandType.values()).forEach(c -> {
            sb.append("/");
            sb.append(c.name().toLowerCase());
            sb.append(" - ");
            sb.append(c.getDescription());
            sb.append("\n");
            sb.append("\n");
        });


        SendMessage inviteMessage = SendMessage.builder()
                .chatId(chat.getId().toString())
                .text(sb.toString())
                .build();

        bot.silent().execute(inviteMessage);
    }

    @Override
    public CommandType getProcessorType() {
        return CommandType.HELP;
    }
}
