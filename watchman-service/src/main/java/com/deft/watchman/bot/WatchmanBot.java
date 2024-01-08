package com.deft.watchman.bot;


import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.processor.ChatUpdateProcessor;
import com.deft.watchman.processor.ProcessorType;
import com.deft.watchman.processor.commands.CommandProcessor;
import com.deft.watchman.processor.commands.CommandType;
import com.deft.watchman.service.ChatUserService;
import com.deft.watchman.service.LinkedInLinkParserService;
import com.deft.watchman.service.WhoisParserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
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
    private final LinkedInLinkParserService linkedInLinkParserService;
    private final WhoisParserService whoisParserService;


    public WatchmanBot(Environment environment,
                       List<ChatUpdateProcessor> processors,
                       ChatUserService chatUserService,
                       LinkedInLinkParserService linkedInLinkParserService,
                       WhoisParserService whoisParserService,
                       List<CommandProcessor> commandProcessors) {
        super(environment.getProperty("telegram.bot.token"), environment.getProperty("telegram.bot.userName"));
        chatProcessorsMap = processors.stream()
                .collect(Collectors.toMap(ChatUpdateProcessor::getProcessorType, p -> p));
        this.chatUserService = chatUserService;
        commandProcessorMap = commandProcessors.stream()
                .collect(Collectors.toMap(CommandProcessor::getProcessorType, p -> p));
        this.linkedInLinkParserService = linkedInLinkParserService;
        this.whoisParserService = whoisParserService;
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
        if (!update.hasMessage() && !update.hasEditedMessage()) {
            return;
        }
        if (update.hasEditedMessage()) {
            Message message = update.getEditedMessage();
            Chat chat = message.getChat();
            User fromUser = message.getFrom();
            Long userId = fromUser.getId();
            Long chatId = chat.getId();
            if (chat.isGroupChat() || chat.isSuperGroupChat()) {
                if (isNewUser(userId, chatId)) {
                    if (whoisParserService.containsValidTag(message.getText())
                            && linkedInLinkParserService.isEnabled()
                            && linkedInLinkParserService.containsValidLinkedInProfileLink(message.getText())) {
                        chatProcessorsMap.get(ProcessorType.VALIDATE_EDIT_FIRST_MESSAGE).processUpdate(this, update);
                        chatProcessorsMap.get(ProcessorType.DELETE_ADD_LINKEDIN_MESSAGE).processUpdate(this, update);
                    } else {
                        banUserAndDeleteMessages(update, ProcessorType.BAN_CHAT_MEMBER, ProcessorType.DELETE_MESSAGE, ProcessorType.DELETE_WELCOME_MESSAGE);
                    }
                }
            }
        } else if (update.getMessage().isCommand()) {
            processCommand(update);
        } else if (isLeftChat(update)) {
            banUserAndDeleteMessages(update, ProcessorType.DELETE_MESSAGE, ProcessorType.DELETE_WELCOME_MESSAGE, ProcessorType.LEAVE_GROUP);
        } else if (isJoinGroup(update)) {
            processJoinGroup(update);
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
                    if (whoisParserService.containsValidTag(message.getText())) {
                        /*
                        If user already sent invite message
                         */
                        if (!isEmptyInviteMessage(userId, chatId)){
                            chatProcessorsMap.get(ProcessorType.DELETE_MESSAGE).processUpdate(this, update);
                            chatProcessorsMap.get(ProcessorType.DONT_USE_TAG).processUpdate(this, update);
                        /*
                        If linkedIn enabled or user sent valid linkedIn URL
                         */
                        } else if (!linkedInLinkParserService.isEnabled() || linkedInLinkParserService.containsValidLinkedInProfileLink(message.getText())) {
                            chatProcessorsMap.get(ProcessorType.VALIDATE_FIRST_MESSAGE).processUpdate(this, update);
                            chatProcessorsMap.get(ProcessorType.DELETE_WELCOME_MESSAGE).processUpdate(this, update);
                        } else if (linkedInLinkParserService.isEnabled()) {
                            chatProcessorsMap.get(ProcessorType.DELETE_WELCOME_MESSAGE).processUpdate(this, update);
                            chatProcessorsMap.get(ProcessorType.ADD_LINKEDIN).processUpdate(this, update);
                        }
                    } else {
                        banUserAndDeleteMessages(update, ProcessorType.BAN_CHAT_MEMBER, ProcessorType.DELETE_MESSAGE, ProcessorType.DELETE_WELCOME_MESSAGE);
                    }
                } else {
                    /*
                    For old users ->
                    if user send message with #whois tag we should remove it and show message like dont do it
                     */
                    if (whoisParserService.containsValidTag(message.getText())) {
                        chatProcessorsMap.get(ProcessorType.DELETE_MESSAGE).processUpdate(this, update);
                        chatProcessorsMap.get(ProcessorType.DONT_USE_TAG).processUpdate(this, update);
                    }
                    processStatistics(userId, chatId, fromUser, message);
                }
            }
        }
    }

    /**
     * Check message and change statistic
     */
    private void processStatistics(Long userId, Long chatId, User fromUser, Message message) {
        Optional<ChatUser> optionalChatUser = chatUserService.findByUserIdAndChatId(userId, chatId);
        if (optionalChatUser.isEmpty()) {
            chatUserService.createOldUser(fromUser, chatId);
        }
        chatUserService.increaseMessageCount(userId, chatId);
        if (message.isReply()) {
            Message replyToMessage = message.getReplyToMessage();
            User replyToUser = replyToMessage.getFrom();
            Chat chat = replyToMessage.getChat();

            Long replyToUserId = replyToUser.getId();
            Optional<ChatUser> optionalReplyToUser = chatUserService.findByUserIdAndChatId(replyToUserId, chat.getId());
            if (optionalReplyToUser.isEmpty()) {
                chatUserService.createOldUser(replyToUser, chat.getId());
            }
            chatUserService.increaseReplyToCount(userId, chatId);

            chatUserService.increaseReplyFromCount(replyToUserId, chat.getId());
        }
    }

    private void processJoinGroup(Update update) {
        if (update.getMessage().getFrom().getIsBot()) {
            return;
        }
        chatProcessorsMap.get(ProcessorType.JOIN_GROUP).processUpdate(this, update);
        chatProcessorsMap.get(ProcessorType.DELETE_MESSAGE).processUpdate(this, update);
    }

    private void processCommand(Update update) {
        Message message = update.getMessage();
        String text = message.getText() + " ";
        String command = text.substring(1, text.indexOf(" ")).toUpperCase();
        if (EnumUtils.isValidEnum(CommandType.class, command)) {
            commandProcessorMap.get(CommandType.valueOf(command.toUpperCase())).processCommand(this, update);
        }
    }

    private void banUserAndDeleteMessages(Update update, ProcessorType banChatMember, ProcessorType deleteMessage, ProcessorType deleteWelcomeMessage) {
        chatProcessorsMap.get(banChatMember).processUpdate(this, update);
        chatProcessorsMap.get(deleteMessage).processUpdate(this, update);
        chatProcessorsMap.get(deleteWelcomeMessage).processUpdate(this, update);
    }

    private boolean isNewUser(Long userId, Long chatId) {
        // todo save users here. we need it to support new users
        Optional<ChatUser> optionalChatUser = chatUserService.findByUserIdAndChatId(userId, chatId);
        return optionalChatUser.map(ChatUser::isNewUser).orElse(false);
    }

    private boolean isEmptyInviteMessage(Long userId, Long chatId) {
        // todo save users here. we need it to support new users
        Optional<ChatUser> optionalChatUser = chatUserService.findByUserIdAndChatId(userId, chatId);
        return optionalChatUser.map(u -> u.getInviteMessage() == null).orElse(false);
    }

    private static boolean isJoinGroup(Update update) {
        return !update.getMessage().getNewChatMembers().isEmpty();
    }

    private static boolean isLeftChat(Update update) {
        return update.getMessage().getLeftChatMember() != null;
    }

}
