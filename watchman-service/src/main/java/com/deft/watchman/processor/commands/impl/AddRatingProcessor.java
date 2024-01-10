package com.deft.watchman.processor.commands.impl;

import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.processor.commands.CommandType;
import com.deft.watchman.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */

@Service
@Transactional
@RequiredArgsConstructor
public class AddRatingProcessor extends BasicCommandProcessor {

    private final ChatUserService chatUserService;

    @Override
    public void processCommand(AbilityBot bot, Update update) {
        Message message = update.getMessage();
        Chat chat = message.getChat();
        Optional<MessageEntity> mention = message.getEntities().stream().filter(e -> e.getType().equals("mention")).findFirst();
        if (mention.isPresent()) {
            MessageEntity messageEntity = mention.get();
            String text = messageEntity.getText().substring(1);
            Optional<ChatUser> optionalChatUser = chatUserService.findByUserNameAndChatId(text, chat.getId());
            if (optionalChatUser.isPresent()) {
                ChatUser chatUser = optionalChatUser.get();
                chatUser.setRating(chatUser.getRating() + 1);
            }
        }


    }

    @Override
    public CommandType getProcessorType() {
        return CommandType.ADD_RATING;
    }
}
