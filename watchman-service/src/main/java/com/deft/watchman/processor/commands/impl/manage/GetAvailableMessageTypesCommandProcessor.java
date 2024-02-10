package com.deft.watchman.processor.commands.impl.manage;

import com.deft.watchman.data.entity.postgres.ChatSettings;
import com.deft.watchman.data.entity.postgres.MessageType;
import com.deft.watchman.processor.commands.CommandType;
import com.deft.watchman.processor.commands.impl.BasicCommandProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

/**
 * @author Sergey Golitsyn
 * created on 08.02.2024
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class GetAvailableMessageTypesCommandProcessor extends BasicCommandProcessor {

    private static final List<String> values = List.of(MessageType.JOIN_GROUP_MESSAGE.toString(),
            MessageType.JOIN_GROUP_MESSAGE_WITHOUT_LINKEDIN.toString());

    @Override
    public String getResultString(List<String> users) {
        StringBuilder sb = new StringBuilder();
        users.forEach(el -> {
            sb.append("/");
            sb.append(CommandType.GET_DETAILED_MESSAGE.toString().toLowerCase());
            sb.append(" ");
            sb.append(el);
            sb.append("\n");
        });
        return sb.toString();
    }

    @Override
    public void processCommand(AbilityBot bot, Update update, ChatSettings chatSettings) {
        try {
            Message message = update.getMessage();
            Chat chat = message.getChat();
            User from = message.getFrom();
            if (bot.isGroupAdmin(chat.getId(), from.getId())) {
                String sb = "Message types:" +
                        "\n" +
                        getResultString(values);

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
