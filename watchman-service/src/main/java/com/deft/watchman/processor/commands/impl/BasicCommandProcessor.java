package com.deft.watchman.processor.commands.impl;

import com.deft.watchman.processor.commands.CommandProcessor;

import java.util.List;

/**
 * @author Sergey Golitsyn
 * created on 10.01.2024
 */
public abstract class BasicCommandProcessor implements CommandProcessor {

    @Override
    public String getResultString(List<String> users) {
        StringBuilder sb = new StringBuilder();
        users.forEach(s -> {
            sb.append("@");
            sb.append(s);
            sb.append("\n");
        });
        return sb.toString();
    }

    @Override
    public String getCommandString(String message) {
        return "/" + getProcessorType().toString().toLowerCase() + " ";
    }
}
