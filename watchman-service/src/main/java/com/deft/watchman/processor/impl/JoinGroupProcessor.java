package com.deft.watchman.processor.impl;

import com.deft.watchman.data.entity.postgres.*;
import com.deft.watchman.mapper.ChatUserMapper;
import com.deft.watchman.processor.ProcessorType;
import com.deft.watchman.repository.postgres.MessageDictionaryRepository;
import com.deft.watchman.service.ChatMessageDictionaryService;
import com.deft.watchman.service.ChatUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 21.12.2023
 */

@Slf4j
@Component
@Transactional
public class JoinGroupProcessor extends BaseDictionaryChatUpdateProcessor {

    private final ChatUserService chatUserService;
    private final ChatUserMapper chatUserMapper;

    private static final String DEFAULT_MESSAGE = "Welcome to the group %s! Please introduce yourself by typing #whois.";

    public JoinGroupProcessor(ChatUserService chatUserService,
                              ChatUserMapper chatUserMapper,
                              MessageDictionaryRepository messageDictionaryRepository,
                              ChatMessageDictionaryService chatMessageDictionaryService) {
        super(messageDictionaryRepository, chatMessageDictionaryService);
        this.chatUserService = chatUserService;
        this.chatUserMapper = chatUserMapper;
    }

    @Override
    public void processUpdate(AbilityBot bot, Update update, ChatSettings settings) {
        Message message = update.getMessage();
        Chat chat = message.getChat();
        LanguageType languageType = settings.getChatLanguage();

        MessageType messageType;
        if (settings.isLinkedinEnable()) {
            messageType = MessageType.JOIN_GROUP_MESSAGE;
        } else {
            messageType = MessageType.JOIN_GROUP_MESSAGE_WITHOUT_LINKEDIN;
        }
        ChatMessageDictionary chatMessageDictionary = super
                .getMessageDictionary(
                        chat.getId(),
                        messageType,
                        languageType,
                        DEFAULT_MESSAGE
                );


        // Check if the new member joined the group
        if (chat.isGroupChat() || chat.isSuperGroupChat()) {
            message.getNewChatMembers().forEach(user -> {
                    Optional<ChatUser> optionalChatUser = chatUserService.findByUserIdAndChatId(user.getId(), chat.getId());
                    ChatUser chatUser;
                    if (optionalChatUser.isEmpty()){
                        chatUser = chatUserMapper.mapToEntity(user);
                        chatUser.setChatId(chat.getId());
                        chatUser = chatUserService.save(chatUser);
                    } else{
                        chatUser = optionalChatUser.get();
                    }
                    chatUser.setNewUser(true);
                    chatUser.setLeave(false);
                    chatUser.setJoinGroupTime(Instant.now());

                String formatted = String.format(chatMessageDictionary.getMessage(), user.getFirstName(), user.getUserName());
                    // Send an invite message
                    if (chatUser.isNewUser()) {
                        SendMessage inviteMessage = SendMessage.builder()
                                .chatId(chat.getId().toString())
                                .text(formatted)
                                .build();
                        Optional<Message> execute = bot.silent().execute(inviteMessage);
                        if (execute.isPresent()){
                            chatUser.setWelcomeMessageId(execute.get().getMessageId());
                        }
                    }
            });
        }
    }

    @Override
    public ProcessorType getProcessorType() {
        return ProcessorType.JOIN_GROUP;
    }
}
