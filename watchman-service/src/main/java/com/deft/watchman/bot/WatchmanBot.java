package com.deft.watchman.bot;


import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.processor.ChatUpdateProcessor;
import com.deft.watchman.processor.ProcessorType;
import com.deft.watchman.service.ChatUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sergey Golitsyn
 * created on 21.12.2023
 */

@Slf4j
@Component
public class WatchmanBot extends AbilityBot {

    private final Map<ProcessorType, ChatUpdateProcessor> chatProcessorsMap;
    private final ChatUserService chatUserService;


    public WatchmanBot(Environment environment, List<ChatUpdateProcessor> processors, ChatUserService chatUserService) {
        super(environment.getProperty("telegram.bot.token"), environment.getProperty("telegram.bot.userName"));

        chatProcessorsMap = processors.stream()
                .collect(Collectors.toMap(ChatUpdateProcessor::getProcessorType, p -> p));
        this.chatUserService = chatUserService;
    }

    @Override
    public long creatorId() {
        return 1L;
    }

    /**
     * todo give 10-30 mins for users to send invite link
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            return;
        }

        if (isLeftChat(update)) {
            chatProcessorsMap.get(ProcessorType.DELETE_MESSAGE).processUpdate(this, update);
            chatProcessorsMap.get(ProcessorType.DELETE_WELCOME_MESSAGE).processUpdate(this, update);
            chatProcessorsMap.get(ProcessorType.LEAVE_GROUP).processUpdate(this, update);
        } else if (isJoinGroup(update)) {
            chatProcessorsMap.get(ProcessorType.JOIN_GROUP).processUpdate(this, update);
            chatProcessorsMap.get(ProcessorType.DELETE_MESSAGE).processUpdate(this, update);
        } else if (update.getMessage().hasText()) {
            Message message = update.getMessage();
            Chat chat = message.getChat();
            User fromUser = message.getFrom();
            Long userId = fromUser.getId();

            if (chat.isGroupChat() || chat.isSuperGroupChat()) {
                // Check if the user has written the welcome message
                Long chatId = chat.getId();
                if (isNewUser(userId, chatId) && isUserSentMessageWithTag(message) && isUserSentMessageWithLinkedInLink(message)) {
                    if (isUserSentMessageWithTag(message) && isUserSentMessageWithLinkedInLink(message)){
                        chatProcessorsMap.get(ProcessorType.VALIDATE_FIRST_MESSAGE).processUpdate(this, update);
                        chatProcessorsMap.get(ProcessorType.DELETE_WELCOME_MESSAGE).processUpdate(this, update);
                    } else if (isUserSentMessageWithTag(message)){
                        // todo add message to add linkedIn link or will be banned
                    }
                } else if (isNewUser(userId, chatId)) {
                    chatProcessorsMap.get(ProcessorType.BAN_CHAT_MEMBER).processUpdate(this, update);
                    chatProcessorsMap.get(ProcessorType.DELETE_MESSAGE).processUpdate(this, update);
                    chatProcessorsMap.get(ProcessorType.DELETE_WELCOME_MESSAGE).processUpdate(this, update);
                } else if (message.getText().toLowerCase().contains("#whois")) {
                    // todo say that you were introduced
                    //  try to reply introduce message

                }
            }
        }
    }

    private boolean isNewUser(Long userId, Long chatId) {
        // todo save users here. we need it to support new users
        Optional<ChatUser> optionalChatUser = chatUserService.findByUserIdAndChatId(userId, chatId);
        return optionalChatUser.map(ChatUser::isNewUser).orElse(false);
    }

    private boolean isUserSentMessageWithTag(Message message) {
        // todo move to db constant
        return message.getText().toLowerCase().contains("#whois");
    }

    private boolean isUserSentMessageWithLinkedInLink(Message message) {
        // todo move to db constant
        return message.getText().toLowerCase().contains("https://www.linkedin.com/in/");
    }

    private static boolean isJoinGroup(Update update) {
        return !update.getMessage().getNewChatMembers().isEmpty();
    }

    private static boolean isLeftChat(Update update) {
        return update.getMessage().getLeftChatMember() != null;
    }


}
