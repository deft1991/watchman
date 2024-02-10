package com.deft.watchman.processor.commands.impl;

import com.deft.watchman.data.entity.postgres.ChatSettings;
import com.deft.watchman.data.entity.postgres.MessageType;
import com.deft.watchman.processor.commands.CommandType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Arrays;

/**
 * @author Sergey Golitsyn
 * created on 08.02.2024
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class GetAvailableMessageTypesCommandProcessor extends BasicCommandProcessor {

    @Override
    public void processCommand(AbilityBot bot, Update update, ChatSettings chatSettings) {
        try {
            Message message = update.getMessage();
            Chat chat = message.getChat();
            User from = message.getFrom();
            if (bot.isGroupAdmin(chat.getId(), from.getId())) {
                MessageType[] values = MessageType.values();
                String sb = "Message types:" +
                        "\n" +
                        getResultString(Arrays.toString(values));

                SendMessage sendMessage = SendMessage.builder().chatId(chat.getId().toString()).text(sb).build();

                bot.silent().execute(sendMessage);
            }
        } catch (Exception ex) {
            log.warn("Err in " + this.getClass().getName() + " ex: {}", ex.getMessage());
        }
    }

    @Override
    public CommandType getProcessorType() {
        return CommandType.GET_AVAILABLE_MESSAGE_TYPES;
    }
}
