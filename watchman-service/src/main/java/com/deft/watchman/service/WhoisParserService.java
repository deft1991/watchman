package com.deft.watchman.service;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */
public interface WhoisParserService {

    String extractTag(String input);
    boolean containsValidTag(String input);

    boolean isEnabled();
}
