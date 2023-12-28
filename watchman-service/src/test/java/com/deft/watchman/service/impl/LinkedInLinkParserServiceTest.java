package com.deft.watchman.service.impl;

import com.deft.watchman.service.LinkedInLinkParserService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LinkedInLinkParserServiceTest {

    @ParameterizedTest
    @MethodSource("linkedInURLData")
    void checkIsValidLinkedInURL(String input, Boolean expected) {
        LinkedInLinkParserService service = new LinkedInLinkParserServiceImpl();
        ReflectionTestUtils.setField(service, "textPattern", "(https?://)?(www\\.)?linkedin\\.com/in/\\w+/?");
        boolean actual = service.containsValidLinkedInProfileLink(input);
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
}
