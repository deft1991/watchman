package com.deft.watchman.processor.commands.impl;

import com.deft.watchman.data.entity.postgres.ChatSettings;
import com.deft.watchman.processor.commands.CommandType;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */

@Service
public class FeedbackCommandProcessor extends BasicCommandProcessor {

    @Override
    public void processCommand(AbilityBot bot, Update update, ChatSettings chatSettings) {
        Message message = update.getMessage();
        Chat chat = message.getChat();

        String text = """
                FaangTalk Anonymous Feedback/Story Form:
                https://forms.gle/rG4UAyKTyMiyffnc7
                                
                #feedback
                """;

        SendMessage inviteMessage = SendMessage.builder()
                .chatId(chat.getId().toString())
                .text(text)
                .build();

        bot.silent().execute(inviteMessage);
    }

    @Override
    public CommandType getProcessorType() {
        return CommandType.FEEDBACK;
    }
}
