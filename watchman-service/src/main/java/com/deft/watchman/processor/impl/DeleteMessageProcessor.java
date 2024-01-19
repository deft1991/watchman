package com.deft.watchman.processor.impl;

import com.deft.watchman.data.entity.postgres.ChatSettings;
import com.deft.watchman.processor.ChatUpdateProcessor;
import com.deft.watchman.processor.ProcessorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Sergey Golitsyn
 * created on 21.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteMessageProcessor implements ChatUpdateProcessor {

    /**
     * Delete the Telegram's default message about user leaving
     */
    @Override
    public void processUpdate(AbilityBot bot, Update update, ChatSettings settings) {
        Message message = update.getMessage();
        Message editedMessage = update.getEditedMessage();
        DeleteMessage deleteMessage = null;
        if (message != null) {
            deleteMessage = DeleteMessage.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .build();
        } else if (editedMessage != null) {
            deleteMessage = DeleteMessage.builder()
                    .chatId(editedMessage.getChatId())
                    .messageId(editedMessage.getMessageId())
                    .build();
        }
        if (deleteMessage != null){
            bot.silent().execute(deleteMessage);
        }
    }

    @Override
    public ProcessorType getProcessorType() {
        return ProcessorType.DELETE_MESSAGE;
    }
}
