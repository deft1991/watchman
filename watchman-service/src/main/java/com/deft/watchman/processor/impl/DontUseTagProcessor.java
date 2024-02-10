package com.deft.watchman.processor.impl;

import com.deft.watchman.data.entity.postgres.ChatMessageDictionary;
import com.deft.watchman.data.entity.postgres.ChatSettings;
import com.deft.watchman.data.entity.postgres.MessageType;
import com.deft.watchman.processor.ProcessorType;
import com.deft.watchman.repository.postgres.MessageDictionaryRepository;
import com.deft.watchman.service.ChatMessageDictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * @author Sergey Golitsyn
 * created on 21.12.2023
 */

@Slf4j
@Component
@Transactional
public class DontUseTagProcessor extends BaseDictionaryChatUpdateProcessor {

    private static final String DEFAULT_MESSAGE = "Hi there %s, don't use #whois in your messages. Hm, you know, you can, but you'll be banned =)";

    public DontUseTagProcessor(MessageDictionaryRepository messageDictionaryRepository,
                               ChatMessageDictionaryService chatMessageDictionaryService) {
        super(messageDictionaryRepository, chatMessageDictionaryService);
    }

    @Override
    public void processUpdate(AbilityBot bot, Update update, ChatSettings settings) {
        Message message = update.getMessage();
        Chat chat = message.getChat();
        User user = message.getFrom();

        ChatMessageDictionary messageDictionary = super
                .getMessageDictionary(
                        chat.getId(),
                        MessageType.DONT_USE_TAG,
                        settings.getChatLanguage(),
                        DEFAULT_MESSAGE
                );
        String formatted = String.format(messageDictionary.getMessage(), user.getFirstName());

        SendMessage sendMessage = SendMessage.builder()
                .chatId(chat.getId().toString())
                .text(formatted)
                .build();
        bot.silent().execute(sendMessage);


    }

    @Override
    public ProcessorType getProcessorType() {
        return ProcessorType.DONT_USE_TAG;
    }
}
