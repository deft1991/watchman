package com.deft.watchman.processor.impl;

import com.deft.watchman.data.entity.postgres.ChatMessageDictionary;
import com.deft.watchman.data.entity.postgres.ChatSettings;
import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.data.entity.postgres.MessageType;
import com.deft.watchman.mapper.ChatUserMapper;
import com.deft.watchman.processor.ProcessorType;
import com.deft.watchman.repository.postgres.MessageDictionaryRepository;
import com.deft.watchman.service.ChatMessageDictionaryService;
import com.deft.watchman.service.ChatUserService;
import com.deft.watchman.service.LinkedInLinkParserService;
import lombok.extern.slf4j.Slf4j;
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
public class ValidateFirstMessageProcessor extends BaseDictionaryChatUpdateProcessor {

    private final ChatUserService chatUserService;
    private final ChatUserMapper chatUserMapper;
    private final LinkedInLinkParserService linkedInLinkParserService;

    private static final String DEFAULT_MESSAGE = "Welcome to the group %s! Please introduce yourself by typing #whois.";

    public ValidateFirstMessageProcessor(ChatUserService chatUserService,
                                         MessageDictionaryRepository messageDictionaryRepository,
                                         ChatUserMapper chatUserMapper,
                                         LinkedInLinkParserService linkedInLinkParserService,
                                         ChatMessageDictionaryService chatMessageDictionaryService) {
        super(messageDictionaryRepository, chatMessageDictionaryService);
        this.chatUserService = chatUserService;
        this.chatUserMapper = chatUserMapper;
        this.linkedInLinkParserService = linkedInLinkParserService;
    }


    @Override
    public void processUpdate(AbilityBot bot, Update update, ChatSettings settings) {
        Message message = update.getMessage();
        Chat chat = message.getChat();
        User fromUser = message.getFrom();
        Long userId = fromUser.getId();

        ChatMessageDictionary chatMessageDictionary = super
                .getMessageDictionary(
                        chat.getId(),
                        MessageType.WELCOME_MESSAGE,
                        settings.getChatLanguage(),
                        DEFAULT_MESSAGE
                );
        /*
         * Remove user from new list
         * Say hello to new user
         * todo
         *  parse message and find linked in link
         *  try to validate linked in link
         *  save #whois message to reply on it later
         */

        Optional<ChatUser> optionalChatUser = chatUserService.findByUserIdAndChatId(userId, chat.getId());
        ChatUser chatUser = optionalChatUser.orElseGet(() -> chatUserMapper.mapToEntity(fromUser));
        chatUser.setChatId(chat.getId());
        chatUser.setNewUser(false);
        chatUser.setInviteMessage(message.getText());
        chatUserService.save(chatUser);

        String linkedInProfileLink = linkedInLinkParserService.extractLinkedInProfileLink(message.getText());
        chatUser.setLinkedinUrl(linkedInProfileLink);
        // todo we can add other message checks

        String formatted = String.format(chatMessageDictionary.getMessage(), fromUser.getFirstName(), fromUser.getUserName());
        SendMessage inviteMessage = SendMessage.builder()
                .chatId(chat.getId().toString())
                .text(formatted)
                .build();
        bot.silent().execute(inviteMessage);
    }

    @Override
    public ProcessorType getProcessorType() {
        return ProcessorType.VALIDATE_FIRST_MESSAGE;
    }
}
