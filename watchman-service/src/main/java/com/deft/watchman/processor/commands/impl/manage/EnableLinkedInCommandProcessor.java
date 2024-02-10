package com.deft.watchman.processor.commands.impl.manage;

import com.deft.watchman.data.entity.postgres.ChatSettings;
import com.deft.watchman.processor.commands.CommandType;
import com.deft.watchman.processor.commands.impl.BasicCommandProcessor;
import com.deft.watchman.repository.postgres.ChatSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * @author Sergey Golitsyn
 * created on 19.01.2024
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class EnableLinkedInCommandProcessor extends BasicCommandProcessor {

    private final ChatSettingsRepository chatSettingsRepository;

    @Override
    public void processCommand(AbilityBot bot, Update update, ChatSettings chatSettings) {
        try {
            Message message = update.getMessage();
            Chat chat = message.getChat();
            User from = message.getFrom();
            if (bot.isGroupAdmin(chat.getId(), from.getId())) {
                chatSettings.setLinkedinEnable(true);
                chatSettingsRepository.save(chatSettings);
            }
        } catch (Exception ex) {
            log.warn("Err in " + this.getClass().getName() + " ex: {}", ex.getMessage());
        }
    }

    @Override
    public CommandType getProcessorType() {
        return CommandType.ENABLE_LINKEDIN;
    }
}
