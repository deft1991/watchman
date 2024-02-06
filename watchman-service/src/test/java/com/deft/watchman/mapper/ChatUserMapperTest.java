package com.deft.watchman.mapper;

import com.deft.watchman.data.entity.postgres.ChatUser;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Sergey Golitsyn
 * created on 05.02.2024
 */
public class ChatUserMapperTest {

    private final ChatUserMapper mapper = Mappers.getMapper(ChatUserMapper.class);

    @Test
    void testMapToEntity() {
        // Given
        User user = new User();
        user.setIsBot(true);
        user.setId(1L);
        user.setUserName("userName");

        // When
        ChatUser chatUser = mapper.mapToEntity(user);

        // Then
        assertEquals(user.getIsBot(), chatUser.isBot());
        assertEquals(user.getId(), chatUser.getUserId());
        assertEquals(user.getUserName(), chatUser.getUserName());
    }
}
