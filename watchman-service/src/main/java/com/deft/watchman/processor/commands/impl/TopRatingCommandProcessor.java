package com.deft.watchman.processor.commands.impl;

import com.deft.watchman.processor.commands.CommandProcessor;
import com.deft.watchman.processor.commands.CommandType;
import com.deft.watchman.service.ChatUserAggregatedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */

@Service
@RequiredArgsConstructor
public class TopRatingCommandProcessor implements CommandProcessor {

    private final ChatUserAggregatedService chatUserAggregatedService;

    @Override
    public void processCommand(AbilityBot bot, Update update) {
        Message message = update.getMessage();
        Chat chat = message.getChat();

        List<String> topSpeakers = chatUserAggregatedService.getTopRating();
        StringBuilder sb = new StringBuilder();
        sb.append("TOP Rated users:");
        sb.append("\n");
        topSpeakers.forEach(s -> {
            sb.append(s);
            sb.append("\n");
        });

        SendMessage inviteMessage = SendMessage.builder()
                .chatId(chat.getId().toString())
                .text(sb.toString())
                .build();

        bot.silent().execute(inviteMessage);
    }

    @Override
    public CommandType getProcessorType() {
        return CommandType.TOP_RATING;
    }
}
