package com.deft.watchman.service.scheduler;

import com.deft.watchman.WatchmanApplication;
import com.deft.watchman.bot.WatchmanBot;
import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.repository.postgres.ChatUserRepository;
import com.deft.watchman.testcontainer.TestContainerBase;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.abilitybots.api.sender.DefaultSender;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Sergey Golitsyn
 * created on 05.02.2024
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WatchmanApplication.class)
@ActiveProfiles("test")
@ContextConfiguration(initializers = {TestContainerBase.Initializer.class})
@TestPropertySource(properties = "app.scheduling.enable=false")
public class BanUserSchedulerImplTest extends TestContainerBase {

    @SpyBean
    private ChatUserRepository chatUserRepository;

    @MockBean
    private WatchmanBot watchmanBot;

    @Test
    @Transactional
    void banUser_Test() {
        // given
        BanUserSchedulerImpl banUserScheduler = new BanUserSchedulerImpl(chatUserRepository, watchmanBot);

        ChatUser chatUser1 = new ChatUser();
        chatUser1.setChatId(1L);
        chatUser1.setUserId(123L);
        chatUser1.setNewUser(true);
        chatUser1.setLeave(false);
        chatUser1.setJoinGroupTime(Instant.now().minus(1, ChronoUnit.DAYS));

        ChatUser chatUser2 = new ChatUser();
        chatUser2.setChatId(1L);
        chatUser2.setUserId(124L);
        chatUser2.setNewUser(true);
        chatUser2.setLeave(false);
        chatUser2.setJoinGroupTime(Instant.now().plus(1, ChronoUnit.DAYS));
        chatUserRepository.saveAll(List.of(chatUser1, chatUser2));

        SilentSender ss = new SilentSender(new DefaultSender(watchmanBot));
        // when
        when(watchmanBot.silent()).thenReturn(ss);
        when(ss.execute(any(BanChatMember.class))).thenReturn(any());
        banUserScheduler.banUser();
        Optional<ChatUser> byUserIdAndChatId = chatUserRepository.findByUserIdAndChatId(chatUser1.getUserId(), chatUser1.getChatId());
        // then
        verify(chatUserRepository, times(1)).findUsersForBan(anyInt());

        assertNotNull(byUserIdAndChatId);
        assertTrue(byUserIdAndChatId.isPresent());
        assertEquals(chatUser1.getUserId(), byUserIdAndChatId.get().getUserId());
        assertEquals(chatUser1.getChatId(), byUserIdAndChatId.get().getChatId());
        assertTrue(byUserIdAndChatId.get().isLeave());
    }
}
