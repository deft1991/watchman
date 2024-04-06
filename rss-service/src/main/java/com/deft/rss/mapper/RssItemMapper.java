package com.deft.rss.mapper;

import com.apptasticsoftware.rssreader.Item;
import com.deft.rss.data.dto.RssItemDto;
import com.deft.rss.data.entity.ChatRssAdvert;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 30.03.2024
 */

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RssItemMapper {

    @Mapping(target = "title", source = "title", qualifiedByName = "unwrap")
    @Mapping(target = "description", source = "description", qualifiedByName = "unwrap")
    @Mapping(target = "link", source = "link", qualifiedByName = "unwrap")
    RssItemDto mapToDto(Item item);

    RssItemDto mapAdvertToDto(ChatRssAdvert advert);

    List<RssItemDto> mapAdvertToDto(List<ChatRssAdvert> advert);

    @Named("unwrap")
    default <T> T unwrap(Optional<T> optional) {
        return optional.orElse(null);
    }

}
