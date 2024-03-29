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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.Instant;
import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 21.12.2023
 */

@Slf4j
@Component
@Transactional
public class AddLinkedInProcessor extends BaseDictionaryChatUpdateProcessor {

    private final ChatUserService chatUserService;
    private final ChatUserMapper chatUserMapper;

    @Value("${telegram.bot.linkedin.enable:true}")
    private boolean isNeedLinkedIn;

    private static final String DEFAULT_MESSAGE = "Hi there %s, you have to add linkedIn URL. Without it you will be banned. Please change your message with #whois tag";

    public AddLinkedInProcessor(ChatUserService chatUserService,
                                ChatUserMapper chatUserMapper,
                                MessageDictionaryRepository messageDictionaryRepository,
                                ChatMessageDictionaryService chatMessageDictionaryService) {
        super(messageDictionaryRepository, chatMessageDictionaryService);
        this.chatUserService = chatUserService;
        this.chatUserMapper = chatUserMapper;
    }


    @Override
    public void processUpdate(AbilityBot bot, Update update, ChatSettings settings) {
        if (!isNeedLinkedIn) {
            return;
        }
        Message message = update.getMessage();
        Chat chat = message.getChat();
        User user = message.getFrom();

        ChatMessageDictionary messageDictionary = super
                .getMessageDictionary(
                        chat.getId(),
                        MessageType.ADD_LINKEDIN_MESSAGE,
                        settings.getChatLanguage(),
                        DEFAULT_MESSAGE
                );

        // Check if the new member joined the group
        if (chat.isGroupChat() || chat.isSuperGroupChat()) {
            Optional<ChatUser> optionalChatUser = chatUserService.findByUserIdAndChatId(user.getId(), chat.getId());
            ChatUser chatUser;
            if (optionalChatUser.isEmpty()) {
                chatUser = chatUserMapper.mapToEntity(user);
                chatUser.setChatId(chat.getId());
                chatUser = chatUserService.save(chatUser);
            } else {
                chatUser = optionalChatUser.get();
            }
            chatUser.setInviteMessage(message.getText());
            chatUser.setNewUser(true);
            chatUser.setLeave(false);
            chatUser.setJoinGroupTime(Instant.now());

            String formatted = String.format(messageDictionary.getMessage(), user.getFirstName());
            // Send an invite message
            if (chatUser.isNewUser()) {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chat.getId().toString())
                        .text(formatted)
                        .build();
                Optional<Message> execute = bot.silent().execute(sendMessage);
                if (execute.isPresent()) {
                    chatUser.setWelcomeMessageId(execute.get().getMessageId());
                }
            }
        }
    }

    @Override
    public ProcessorType getProcessorType() {
        return ProcessorType.ADD_LINKEDIN;
    }
}
