package com.deft.watchman.service;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */
public interface LinkedInLinkParserService {

    String extractLinkedInProfileLink(String input);
    boolean containsValidLinkedInProfileLink(String input);

}
