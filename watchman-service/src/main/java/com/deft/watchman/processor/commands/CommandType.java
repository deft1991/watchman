package com.deft.watchman.processor.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Sergey Golitsyn
 * created on 21.12.2023
 */

@Getter
@AllArgsConstructor
public enum CommandType {
    // todo maybe move descriptions to params
    HELP("Show all available commands"),
    TOP("Show 5 TOP active users"),
    TOP_SPEAKER("Show TOP 5 Speakers"),
    TOP_REPLY_TO("Show TOP 5 users who likes to reply on messages"),
    TOP_REPLY_FROM("Show TOP 5 users who wrote the most answered messages"),
    ADD_RATING("Show TOP 5 users with the highest rate");

    private final String description;
}
