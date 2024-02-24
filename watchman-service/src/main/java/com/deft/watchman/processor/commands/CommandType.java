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
    HELP("Show all available commands"),
    TOP("Show 5 TOP active users"),
    TOP_SPEAKER("Show TOP 5 Speakers"),
    TOP_REPLY_TO("Show TOP 5 users who likes to reply on messages"),
    TOP_REPLY_FROM("Show TOP 5 users who wrote the most answered messages"),
    ADD_RATING("Add Rating to user. Format /add_rating @user_name"),
    TOP_RATING("Show TOP 5 users with the highest rate"),
    FEEDBACK("Show feedback link"),

//    Admin commands
    ENABLE_LINKEDIN("Enable LinkedIn (Only for Admins)"),
    DISABLE_LINKEDIN("Disable LinkedIn (Only for Admins)"),
    BAN_WAIT_TIME_SECONDS("Set time before BAN user in seconds (Only for Admins)"),
    SET_LANGUAGE("Set chat language. You need to pass param. Available params: RUS/ENG (Only for Admins)"),
    GET_AVAILABLE_MESSAGE_TYPES("Return available message TYPES. Then you need to call /get_detailed_message {TYPE} (Only for Admins)"),
    GET_DETAILED_MESSAGE("Get detailed message by type, for your chat. Example: /get_detailed_message {TYPE}. If you want to change detailed message call:  /change_detailed_message {TYPE} {bla_bla_bla_new_message} (Only for Admins)"),
    CHANGE_DETAILED_MESSAGE("Change detailed message by type, for your chat. Example: /change_detailed_message {TYPE} {bla_bla_bla_new_message} (Only for Admins)");

    private final String description;
}
