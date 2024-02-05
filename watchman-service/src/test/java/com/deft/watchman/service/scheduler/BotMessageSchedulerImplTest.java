package com.deft.watchman.service.scheduler;

import com.deft.watchman.WatchmanApplication;
import com.deft.watchman.bot.WatchmanBot;
import com.deft.watchman.data.entity.postgres.BotMessage;
import com.deft.watchman.repository.postgres.BotMessageRepository;
import com.deft.watchman.service.BotMessageService;
import com.deft.watchman.testcontainer.TestContainerBase;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
public class BotMessageSchedulerImplTest extends TestContainerBase {

    @SpyBean
    private BotMessageRepository botMessageRepository;

    @Autowired
    private BotMessageService botMessageService;


    @MockBean
    private WatchmanBot watchmanBot;

    @Test
    @Transactional
    void banUser_Test() {
        // given
        BotMessageSchedulerImpl scheduler = new BotMessageSchedulerImpl(watchmanBot, botMessageService);

        BotMessage bm1 = new BotMessage(1L, "msg", Instant.now(), false);
        BotMessage bm2 = new BotMessage(2L, "msg", Instant.now(), false);
        BotMessage bm3 = new BotMessage(2L, "msg", Instant.now(), true);
        botMessageRepository.saveAll(List.of(bm1, bm2, bm3));

        SilentSender ss = new SilentSender(new DefaultSender(watchmanBot));
        when(watchmanBot.silent()).thenReturn(ss);

        // when
        scheduler.sendBotMessages();

        // then
        verify(botMessageRepository, times(1)).findAllForSend();
        verify(botMessageRepository, times(1)).markAsSent(anyList());

        List<BotMessage> allForSend = botMessageRepository.findAllForSend();

        assertNotNull(allForSend);
        assertTrue(allForSend.isEmpty());
    }
}
