package com.deft.watchman.processor;

import com.deft.watchman.data.entity.postgres.ChatSettings;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Sergey Golitsyn
 * created on 21.12.2023
 */
public interface ChatUpdateProcessor {

    void processUpdate(AbilityBot bot, Update update, ChatSettings settings);

    ProcessorType getProcessorType();
}
