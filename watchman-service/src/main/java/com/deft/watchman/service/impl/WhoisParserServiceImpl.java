package com.deft.watchman.service.impl;

import com.deft.watchman.service.WhoisParserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */

@Service
public class WhoisParserServiceImpl implements WhoisParserService {

    @Value("${telegram.bot.whois.enable:true}")
    private boolean isEnable;

    @Value("${telegram.bot.whois.pattern}")
    private String textPattern;

    @Override
    public String extractTag(String input) {
        return ParserServiceHelper.extractTextByPattern(input, textPattern);
    }

    @Override
    public boolean containsValidTag(String input) {
        return ParserServiceHelper.isValidInput(input, textPattern);
    }

    /**
     * Todo add code for it later
     */
    @Override
    public boolean isEnabled() {
        return isEnable;
    }
}
