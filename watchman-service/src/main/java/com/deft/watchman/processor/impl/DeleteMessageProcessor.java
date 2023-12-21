package com.deft.watchman.processor.impl;

import com.deft.watchman.processor.ChatUpdateProcessor;
import com.deft.watchman.processor.ProcessorType;
import com.deft.watchman.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Sergey Golitsyn
 * created on 21.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteMessageProcessor implements ChatUpdateProcessor {

    private final ChatUserService chatUserService;

    /**
     * Delete the Telegram's default message about user leaving
     */
    @Override
    public void processUpdate(AbilityBot bot, Update update) {
        Message message = update.getMessage();

        DeleteMessage deleteMessage = DeleteMessage.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .build();
        try {
            bot.execute(deleteMessage);
        } catch (TelegramApiException e) {
            log.error("Err: {}", e.getMessage());
        }
    }

    @Override
    public ProcessorType getProcessorType() {
        return ProcessorType.DELETE_MESSAGE;
    }
}
