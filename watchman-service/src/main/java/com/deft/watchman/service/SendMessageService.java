package com.deft.watchman.service;

/**
 * @author Sergey Golitsyn
 * created on 19.01.2024
 */
public interface SendMessageService {

    void sendMessage(long chatId, String message);
}
