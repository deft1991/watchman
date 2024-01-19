package com.deft.watchman.processor.commands;

import com.deft.watchman.data.entity.postgres.ChatSettings;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */
public interface CommandProcessor {

    void processCommand(AbilityBot bot, Update update, ChatSettings chatSettings);

    CommandType getProcessorType();

    String getResultString(List<String> users);
}
