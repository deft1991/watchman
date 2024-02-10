package com.deft.watchman.mapper;

import com.deft.watchman.data.entity.postgres.ChatMessageDictionary;
import com.deft.watchman.data.entity.postgres.MessageDictionary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * @author Sergey Golitsyn
 * created on 05.06.2023
 */

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMessageDictionaryMapper {

    @Mapping(target = "chatId", expression = "java(chatId)")
    ChatMessageDictionary mapToEntity(MessageDictionary messageDictionary, long chatId);

    default List<ChatMessageDictionary> mapToEntity(Iterable<MessageDictionary> messageDictionary, long chatId) {
        return StreamSupport.stream(messageDictionary.spliterator(), false)
                .map(message -> mapToEntity(message, chatId))
                .toList();
    }

}
