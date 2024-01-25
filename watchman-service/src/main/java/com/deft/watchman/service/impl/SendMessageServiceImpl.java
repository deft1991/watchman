package com.deft.watchman.service.impl;

import com.deft.watchman.bot.WatchmanBot;
import com.deft.watchman.service.SendMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * @author Sergey Golitsyn
 * created on 19.01.2024
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMessageServiceImpl implements SendMessageService {

    private final WatchmanBot watchmanBot;

    @Override
    public void sendMessage(long chatId, String message) {
        SendMessage inviteMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();
        watchmanBot.silent().execute(inviteMessage);
    }
}
