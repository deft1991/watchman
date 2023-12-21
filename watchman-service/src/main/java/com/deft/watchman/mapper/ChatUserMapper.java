package com.deft.watchman.mapper;

import com.deft.watchman.data.entity.postgres.ChatUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * @author Sergey Golitsyn
 * created on 05.06.2023
 */

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatUserMapper {
    @Mapping(target = "bot", source = "isBot")
    @Mapping(target = "userId", source = "id")
    @Mapping(target = "id", ignore = true)
    ChatUser mapToEntity(User user);

}
