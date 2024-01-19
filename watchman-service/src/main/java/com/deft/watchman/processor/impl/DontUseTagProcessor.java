package com.deft.watchman.processor.impl;

import com.deft.watchman.data.entity.postgres.ChatSettings;
import com.deft.watchman.data.entity.postgres.MessageDictionary;
import com.deft.watchman.data.entity.postgres.MessageType;
import com.deft.watchman.processor.ChatUpdateProcessor;
import com.deft.watchman.processor.ProcessorType;
import com.deft.watchman.repository.postgres.MessageDictionaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 21.12.2023
 */

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class DontUseTagProcessor implements ChatUpdateProcessor {

    private final MessageDictionaryRepository messageDictionaryRepository;

    @Override
    public void processUpdate(AbilityBot bot, Update update, ChatSettings settings) {
        Message message = update.getMessage();
        Chat chat = message.getChat();
        User user = message.getFrom();

        Optional<MessageDictionary> byType = messageDictionaryRepository.findByType(MessageType.DONT_USE_TAG);
        MessageDictionary messageDictionary = getMessageDictionary(byType);
        String formatted = String.format(messageDictionary.getMessage(), user.getFirstName());

        SendMessage sendMessage = SendMessage.builder()
                .chatId(chat.getId().toString())
                .text(formatted)
                .build();
        bot.silent().execute(sendMessage);


    }

    @NotNull
    private static MessageDictionary getMessageDictionary(Optional<MessageDictionary> byType) {
        MessageDictionary messageDictionary;
        if (byType.isEmpty()) {
            log.warn("Message with type {} is empty", MessageType.DONT_USE_TAG);
            messageDictionary = new MessageDictionary();
            messageDictionary.setMessage("Hi there %s, don't use #whois in your messages. Hm, you know, you can, but you'll be banned =)");
        } else {
            messageDictionary = byType.get();
        }
        return messageDictionary;
    }

    @Override
    public ProcessorType getProcessorType() {
        return ProcessorType.DONT_USE_TAG;
    }
}
