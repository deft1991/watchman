package com.deft.watchman.service.impl;

import com.deft.watchman.service.WhoisParserService;
import com.deft.watchman.util.ParserServiceHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */

@Service
public class WhoisParserServiceImpl implements WhoisParserService {

    @Value("${telegram.bot.whois.pattern}")
    private String textPattern;

    @Override
    public boolean containsValidTag(String input) {
        return ParserServiceHelper.isValidInput(input, textPattern);
    }

}
