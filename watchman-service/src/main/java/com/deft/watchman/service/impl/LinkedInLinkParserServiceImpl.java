package com.deft.watchman.service.impl;

import com.deft.watchman.service.LinkedInLinkParserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */

@Service
public class LinkedInLinkParserServiceImpl implements LinkedInLinkParserService {

    @Value("${telegram.bot.linkedin.pattern}")
    private String textPattern;

    @Override
    public String extractLinkedInProfileLink(String input) {
       return ParserServiceHelper.extractTextByPattern(input, textPattern);
    }

    @Override
    public boolean containsValidLinkedInProfileLink(String input) {
        return ParserServiceHelper.isValidInput(input, textPattern);
    }

}
