package com.deft.watchman.processor.commands.impl.manage;

import com.deft.watchman.data.entity.postgres.ChatMessageDictionary;
import com.deft.watchman.data.entity.postgres.ChatSettings;
import com.deft.watchman.data.entity.postgres.MessageType;
import com.deft.watchman.processor.commands.CommandType;
import com.deft.watchman.processor.commands.impl.BasicCommandProcessor;
import com.deft.watchman.service.ChatMessageDictionaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 08.02.2024
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeDetailedMessageCommandProcessor extends BasicCommandProcessor {

    private final ChatMessageDictionaryService chatMessageDictionaryService;

    @Override
    public void processCommand(AbilityBot bot, Update update, ChatSettings chatSettings) {
        try {
            Message message = update.getMessage();
            Chat chat = message.getChat();
            User from = message.getFrom();
            if (bot.isGroupAdmin(chat.getId(), from.getId())) {
                StringBuilder sb = new StringBuilder();
                String text = message.getText();
                String commandText = getCommandString(text);
                text = text.replaceAll(commandText, "").trim();
                String type = text.substring(0, text.indexOf(" ")).trim().toUpperCase();

                if (EnumUtils.isValidEnum(MessageType.class, type)) {
                    Optional<ChatMessageDictionary> optional = chatMessageDictionaryService.findByChatIdAndTypeAndLanguage(
                            chat.getId(),
                            MessageType.valueOf(type),
                            chatSettings.getChatLanguage()
                    );
                    if (optional.isPresent()) {
                        String finalMessage = getFinalMessage(optional.get(), text);
                        chatMessageDictionaryService.save(optional.get());

                        sb.append("New message for ");
                        sb.append(MessageType.valueOf(type));
                        sb.append(" is: ");
                        sb.append("\n");
                        sb.append(finalMessage);
                    }
                } else {
                    log.warn("Incorrect input message type: {}", type);
                    sb.append("Incorrect input. Call /GET_DETAILED_MESSAGE to view available message types");
                }

                SendMessage sendMessage = SendMessage
                        .builder()
                        .chatId(chat.getId().toString())
                        .text(sb.toString())
                        .build();
                bot.silent().execute(sendMessage);

            }
        } catch (Exception ex) {
            log.warn("Err in " + this.getClass().getName() + " ex: {}", ex.getMessage());
        }
    }

    @NotNull
    private static String getFinalMessage(ChatMessageDictionary chatMessageDictionary, String text) {
        String oldMessage = chatMessageDictionary.getMessage();
        String header = oldMessage.substring(0, oldMessage.indexOf("\n")).trim();
        String ending = oldMessage.substring(oldMessage.lastIndexOf("\n")).trim();

        String newMessage = text.substring(text.indexOf(" ")).trim();
        String finalMessage = header + "\n\n" + newMessage + "\n\n" + ending;
        chatMessageDictionary.setMessage(finalMessage);
        return finalMessage;
    }

    @Override
    public CommandType getProcessorType() {
        return CommandType.CHANGE_DETAILED_MESSAGE;
    }
}
