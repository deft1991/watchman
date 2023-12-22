package com.deft.watchman.bot;


import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.processor.ChatUpdateProcessor;
import com.deft.watchman.processor.ProcessorType;
import com.deft.watchman.processor.commands.CommandProcessor;
import com.deft.watchman.processor.commands.CommandType;
import com.deft.watchman.service.ChatUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Sergey Golitsyn
 * created on 21.12.2023
 */

@Slf4j
@Component
public class WatchmanBot extends AbilityBot {

    private final Map<ProcessorType, ChatUpdateProcessor> chatProcessorsMap;
    private final Map<CommandType, CommandProcessor> commandProcessorMap;
    private final ChatUserService chatUserService;

    @Value("${telegram.bot.linkedin.enable:true}")
    private boolean isNeedLinkedIn;


    public WatchmanBot(Environment environment,
                       List<ChatUpdateProcessor> processors,
                       ChatUserService chatUserService,
                       List<CommandProcessor> commandProcessors) {
        super(environment.getProperty("telegram.bot.token"), environment.getProperty("telegram.bot.userName"));
        chatProcessorsMap = processors.stream()
                .collect(Collectors.toMap(ChatUpdateProcessor::getProcessorType, p -> p));
        this.chatUserService = chatUserService;
        commandProcessorMap = commandProcessors.stream()
                .collect(Collectors.toMap(CommandProcessor::getProcessorType, p -> p));
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
        if (update.hasEditedMessage()) {
            Message message = update.getEditedMessage();
            Chat chat = message.getChat();
            User fromUser = message.getFrom();
            Long userId = fromUser.getId();
            Long chatId = chat.getId();
            if (chat.isGroupChat() || chat.isSuperGroupChat()) {
                if (isNewUser(userId, chatId)) {
                    if (isUserSentMessageWithTag(message) && isNeedLinkedIn && isUserSentMessageWithLinkedInLink(message)) {
                        chatProcessorsMap.get(ProcessorType.VALIDATE_FIRST_MESSAGE).processUpdate(this, update);
                        chatProcessorsMap.get(ProcessorType.DELETE_WELCOME_MESSAGE).processUpdate(this, update);
                    } else {
                        chatProcessorsMap.get(ProcessorType.BAN_CHAT_MEMBER).processUpdate(this, update);
                        chatProcessorsMap.get(ProcessorType.DELETE_MESSAGE).processUpdate(this, update);
                        chatProcessorsMap.get(ProcessorType.DELETE_WELCOME_MESSAGE).processUpdate(this, update);
                    }
                }
            }
        }
        if (!update.hasMessage()) {
            return;
        }
        if (update.getMessage().isCommand()) {
            Message message = update.getMessage();
            String text = message.getText() + " ";
            String command = text.substring(1, text.indexOf(" ")).toUpperCase();
            if (EnumUtils.isValidEnum(CommandType.class, command)) {
                commandProcessorMap.get(CommandType.valueOf(command.toUpperCase())).processCommand(this, update);
            }
            // todo
            //  support command
            //  help --> list of commands
            //  top --> top 5
            //  top_speaker --> top 5 speakers
            //  top_reply_to -->
            //  top_reply_from -->
            //  add_rating userName --> add rating to user name
        } else if (isLeftChat(update)) {
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
                /*
                if new user
                - check if message has TAG
                - if no ban user
                - if so - then check linked in link in message
                 todo - I need to do it more flexible. I don't like a lot of if cases
                 */
                if (isNewUser(userId, chatId)) {
                    if (isUserSentMessageWithTag(message)) {
                        if (!isNeedLinkedIn || isUserSentMessageWithLinkedInLink(message)) {
                            chatProcessorsMap.get(ProcessorType.VALIDATE_FIRST_MESSAGE).processUpdate(this, update);
                            chatProcessorsMap.get(ProcessorType.DELETE_WELCOME_MESSAGE).processUpdate(this, update);
                        } else if (isNeedLinkedIn) {
                            chatProcessorsMap.get(ProcessorType.DELETE_WELCOME_MESSAGE).processUpdate(this, update);
                            chatProcessorsMap.get(ProcessorType.ADD_LINKEDIN).processUpdate(this, update);
                        }
                    } else {
                        chatProcessorsMap.get(ProcessorType.BAN_CHAT_MEMBER).processUpdate(this, update);
                        chatProcessorsMap.get(ProcessorType.DELETE_MESSAGE).processUpdate(this, update);
                        chatProcessorsMap.get(ProcessorType.DELETE_WELCOME_MESSAGE).processUpdate(this, update);
                    }
                } else {
                    Optional<ChatUser> optionalChatUser = chatUserService.findByUserIdAndChatId(userId, chatId);
                    if (optionalChatUser.isEmpty()) {
                        chatUserService.createOldUser(fromUser, chatId);
                    }
                    chatUserService.increaseMessageCount(userId, chatId);
                    if (message.isReply()) {
                        Message replyToMessage = message.getReplyToMessage();
                        User replyToUser = replyToMessage.getFrom();
                        Chat chat1 = replyToMessage.getChat();

                        Long replyToUserId = replyToUser.getId();
                        Optional<ChatUser> optionalReplyToUser = chatUserService.findByUserIdAndChatId(replyToUserId, chat1.getId());
                        if (optionalReplyToUser.isEmpty()) {
                            chatUserService.createOldUser(replyToUser, chat1.getId());
                        }
                        chatUserService.increaseReplyToCount(userId, chatId);

                        chatUserService.increaseReplyFromCount(replyToUserId, chat1.getId());
                    }
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
