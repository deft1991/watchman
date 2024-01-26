package com.deft.watchman.service;

import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

/**
 * @author Sergey Golitsyn
 * created on 26.01.2024
 */
public interface BotMessageService {

    List<Triple<Long, String, String>> getMessagesToSend();

    void markAsSent(List<String> sent);
}
