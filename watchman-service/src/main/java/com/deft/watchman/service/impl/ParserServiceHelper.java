package com.deft.watchman.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sergey Golitsyn
 * created on 28.12.2023
 *
 * Helper class. Some static methods to parse strings by pattern
 */
public class ParserServiceHelper {

    public static String extractTextByPattern(String input, String textPattern) {
        // Regular expression to match LinkedIn profile links
        Pattern pattern = Pattern.compile(textPattern);
        Matcher matcher = pattern.matcher(input);

        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            result.append(matcher.group()).append("\n");
        }

        return result.toString().trim();
    }

    public static boolean isValidInput(String input, String textPattern) {
        // Regular expression to match LinkedIn profile links
        Pattern pattern = Pattern.compile(textPattern);
        Matcher matcher = pattern.matcher(input);

        return matcher.find();
    }
}
