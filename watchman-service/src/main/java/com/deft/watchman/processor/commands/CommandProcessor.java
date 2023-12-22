package com.deft.watchman.processor.commands;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */
public interface CommandProcessor {

    void processCommand(AbilityBot bot, Update update);

    CommandType getProcessorType();
}
