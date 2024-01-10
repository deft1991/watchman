package com.deft.watchman.processor.impl;

import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.data.entity.postgres.MessageDictionary;
import com.deft.watchman.data.entity.postgres.MessageType;
import com.deft.watchman.mapper.ChatUserMapper;
import com.deft.watchman.processor.ChatUpdateProcessor;
import com.deft.watchman.processor.ProcessorType;
import com.deft.watchman.repository.postgres.MessageDictionaryRepository;
import com.deft.watchman.service.ChatUserService;
import com.deft.watchman.service.LinkedInLinkParserService;
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
public class ValidateFirstMessageProcessor implements ChatUpdateProcessor {

    private final ChatUserService chatUserService;
    private final MessageDictionaryRepository messageDictionaryRepository;
    private final ChatUserMapper chatUserMapper;
    private final LinkedInLinkParserService linkedInLinkParserService;


    @Override
    public void processUpdate(AbilityBot bot, Update update) {
        Message message = update.getMessage();
        Chat chat = message.getChat();
        User fromUser = message.getFrom();
        Long userId = fromUser.getId();

        Optional<MessageDictionary> byType = messageDictionaryRepository.findByType(MessageType.WELCOME_MESSAGE);
        MessageDictionary messageDictionary = getMessageDictionary(byType);
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

        String formatted = String.format(messageDictionary.getMessage(), fromUser.getFirstName(), fromUser.getUserName());
        SendMessage inviteMessage = SendMessage.builder()
                .chatId(chat.getId().toString())
                .text(formatted)
                .build();
        bot.silent().execute(inviteMessage);
    }

    @NotNull
    private static MessageDictionary getMessageDictionary(Optional<MessageDictionary> byType) {
        MessageDictionary messageDictionary;
        if (byType.isEmpty()) {
            log.warn("Message with type {} is empty", MessageType.JOIN_GROUP_MESSAGE);
            messageDictionary = new MessageDictionary();
            messageDictionary.setMessage("Welcome to the group %s! Please introduce yourself by typing #whois.");
        } else {
            messageDictionary = byType.get();
        }
        return messageDictionary;
    }


    @Override
    public ProcessorType getProcessorType() {
        return ProcessorType.VALIDATE_FIRST_MESSAGE;
    }
}
