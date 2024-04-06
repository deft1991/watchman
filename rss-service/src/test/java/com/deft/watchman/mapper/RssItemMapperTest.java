package com.deft.watchman.mapper;

import com.apptasticsoftware.rssreader.DateTime;
import com.apptasticsoftware.rssreader.Item;
import com.deft.rss.data.dto.RssItemDto;
import com.deft.rss.data.entity.ChatRssAdvert;
import com.deft.rss.mapper.RssItemMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class RssItemMapperTest {

    private final RssItemMapper mapper = Mappers.getMapper(RssItemMapper.class);

    @Test
    void mapToEntity_success() {
        // Given
        Item item = new Item(new DateTime());
        item.setTitle("title");
        item.setDescription("description");
        item.setLink("link");
        item.setAuthor("author");

        // When
        RssItemDto dto = mapper.mapToDto(item);

        // Then
        assertNotNull(dto);
        assertEquals(dto.getTitle(), item.getTitle().orElse(""));
        assertEquals(dto.getDescription(), item.getDescription().orElse(""));
        assertEquals(dto.getLink(), item.getLink().orElse(""));
    }

    @Test
    void mapAdvertToEntity_success() {
        // Given
        ChatRssAdvert item = new ChatRssAdvert();
        item.setTitle("title");
        item.setDescription("description");
        item.setLink("link");

        // When
        RssItemDto dto = mapper.mapAdvertToDto(item);

        // Then
        assertNotNull(dto);
        assertEquals(dto.getTitle(), item.getTitle());
        assertEquals(dto.getDescription(), item.getDescription());
        assertEquals(dto.getLink(), item.getLink());
    }

}
