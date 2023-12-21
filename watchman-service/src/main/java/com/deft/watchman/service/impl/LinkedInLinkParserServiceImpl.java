package com.deft.watchman.service.impl;

import com.deft.watchman.service.LinkedInLinkParserService;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */

@Service
public class LinkedInLinkParserServiceImpl implements LinkedInLinkParserService {
    @Override
    public String extractLinkedInProfileLink(String input) {
        // Regular expression to match LinkedIn profile links
        String regex = "https://www\\.linkedin\\.com/in/\\S+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            result.append(matcher.group()).append("\n");
        }

        return result.toString().trim();
    }
}
