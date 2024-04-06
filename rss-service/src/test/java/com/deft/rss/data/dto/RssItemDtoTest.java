package com.deft.rss.data.dto;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static com.deft.rss.data.dto.RssItemDto.BOLD_DESCRIPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RssItemDtoTest {

    @Test
    void testGetShortDescription_with_long_message() throws Exception {
        // setup
        RssItemDto rssItemDto = new RssItemDto();
        rssItemDto.setDescription("This is a test description for our RssItemDto object.");

        Method method = RssItemDto.class.getDeclaredMethod("getShortText", int.class, String.class);
        method.setAccessible(true);

        // execute
        String shortDescription = (String) method.invoke(rssItemDto, 20, rssItemDto.getDescription());

        // verify
        assertEquals("This is a test descr...", shortDescription);
    }

    @Test
    void testGetShortDescription_with_short_message() throws Exception {
        // setup
        RssItemDto rssItemDto = new RssItemDto();
        rssItemDto.setDescription("description.");

        Method method = RssItemDto.class.getDeclaredMethod("getShortText", int.class, String.class);
        method.setAccessible(true);

        // execute
        String shortDescription = (String) method.invoke(rssItemDto, 20, rssItemDto.getDescription());

        // verify
        assertEquals(rssItemDto.getDescription(), shortDescription);
    }

    @Test
    void testConvertToPrintString_long_description() {
        // setup
        RssItemDto rssItemDto = new RssItemDto();
        rssItemDto.setTitle("Test Title");
        rssItemDto.setLink("Test Link");
        rssItemDto.setDescription("This is a test description for our RssItemDto object.");

        // execute
        String printString = rssItemDto.convertToPrintString(20);

        // verify
        String expected = "[Test Title](Test Link)\n" +
                BOLD_DESCRIPTION + " This is a test descr...\n";
        assertEquals(expected, printString);
    }

    @Test
    void testConvertToPrintString_short_description() {
        // setup
        RssItemDto rssItemDto = new RssItemDto();
        rssItemDto.setTitle("Test Title");
        rssItemDto.setLink("Test Link");
        rssItemDto.setDescription("description.");

        // execute
        String printString = rssItemDto.convertToPrintString(20);

        // verify
        String expected = "[Test Title](Test Link)\n" +
                BOLD_DESCRIPTION + " " + rssItemDto.getDescription() + "\n";
        assertEquals(expected, printString);
    }
}
