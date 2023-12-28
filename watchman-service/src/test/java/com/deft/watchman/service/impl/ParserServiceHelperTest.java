package com.deft.watchman.service.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserServiceHelperTest {

    @ParameterizedTest
    @MethodSource("linkedInURLData")
    void checkIsValidLinkedInURL(String input, Boolean expected) {
        boolean actual = ParserServiceHelper.isValidInput(input, "(https?://)?(www\\.)?linkedin\\.com/in/\\w+/?");
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("whoisData")
    void checkIsValidWhois(String input, Boolean expected) {
        boolean actual = ParserServiceHelper.isValidInput(input, "\\s#whois\\s|\\s#whois$|^#whois\\s|^#whois$");
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> linkedInURLData() {
        return Stream.of(
                Arguments.of("www.linkedin.com/in/xxx/", true),
                Arguments.of("linkedin.com/in/xx/", true),
                Arguments.of("http://www.linkedin.com/in/xxx/", true),
                Arguments.of("https://www.linkedin.com/in/xxx/", true),
                Arguments.of("https://www.linkedin.com/", false),
                Arguments.of("www.linkedin.com/", false),
                Arguments.of("bla bla bla www.linkedin.com/in/xxx/", true),
                Arguments.of("bla bla bla \n bla \n bla www.linkedin.com/in/xxx/", true)
        );
    }

    private static Stream<Arguments> whoisData() {
        return Stream.of(
                Arguments.of("#whois", true),
                Arguments.of("qweqwe #whois", true),
                Arguments.of("#whois qweqwe", true),
                Arguments.of(" #whois ", true),
                Arguments.of(" sdf #whois sdf", true),
                Arguments.of("qwe #whois qwe", true),
                Arguments.of("#whoisqwe", false),
                Arguments.of("qwe#whois", false),
                Arguments.of("qwe#whoisqwe", false),
                Arguments.of("qwewe", false)
        );
    }
}
