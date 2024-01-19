package com.deft.watchman.processor.impl;

import com.deft.watchman.data.entity.postgres.ChatSettings;
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

import java.time.Instant;
import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 21.12.2023
 */

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class JoinGroupProcessor implements ChatUpdateProcessor {

    private final ChatUserService chatUserService;
    private final ChatUserMapper chatUserMapper;
    private final MessageDictionaryRepository messageDictionaryRepository;
    private final LinkedInLinkParserService linkedInLinkParserService;


    @Override
    public void processUpdate(AbilityBot bot, Update update, ChatSettings settings) {
        Optional<MessageDictionary> byType;
        if (settings.isLinkedinEnable()) {
            byType = messageDictionaryRepository.findByType(MessageType.JOIN_GROUP_MESSAGE);
        } else {
            byType = messageDictionaryRepository.findByType(MessageType.JOIN_GROUP_MESSAGE_WITHOUT_LINKEDIN);
        }
        MessageDictionary messageDictionary = getMessageDictionary(byType);

        Message message = update.getMessage();
        Chat chat = message.getChat();

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

                String formatted = String.format(messageDictionary.getMessage(), user.getFirstName(), user.getUserName());
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
        return ProcessorType.JOIN_GROUP;
    }
}
