package com.deft.watchman.testcontainer;

import com.deft.watchman.WatchmanApplication;
import com.deft.watchman.bot.WatchmanBot;
import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.repository.postgres.ChatUserRepository;
import com.deft.watchman.service.impl.ChatUserAggregateServiceImpl;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WatchmanApplication.class)
@ActiveProfiles("test")
@ContextConfiguration(initializers = {ChatUserAggregateServiceImplTest.Initializer.class})
public class ChatUserAggregateServiceImplTest {

    @MockBean
    private WatchmanBot watchmanBot;

    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @BeforeAll
    static void startContainer() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void stopContainer() {
        postgreSQLContainer.stop();
    }

    @Autowired
    private ChatUserAggregateServiceImpl chatUserAggregateService;

    @Autowired
    private ChatUserRepository repository;

    @Test
    @Transactional
    void getTopSpeakers_ReturnsExpectedList() {
        // given
        ChatUser c1 = new ChatUser();
        c1.setMessageCount(100);
        c1.setUserName("1");
        c1.setChatId(1L);
        ChatUser c2 = new ChatUser();
        c2.setMessageCount(50);
        c2.setUserName("2");
        c2.setChatId(1L);
        repository.save(c1);
        repository.save(c2);

        // when
        List<String> topSpeakers = chatUserAggregateService.getTopSpeakers(1L);

        // then
        assertEquals(2, topSpeakers.size());
        assertEquals("1", topSpeakers.get(0));
        assertEquals("2", topSpeakers.get(1));
        // Add more assertions based on your test data
    }

    @Test
    @Transactional
    void getTopReplyTo_ReturnsExpectedList() {
        ChatUser c1 = new ChatUser();
        c1.setMessageCount(100);
        c1.setUserName("1");
        c1.setChatId(1L);
        ChatUser c2 = new ChatUser();
        c2.setMessageCount(50);
        c2.setUserName("2");
        c2.setChatId(1L);
        repository.save(c1);
        repository.save(c2);
        // when
        List<String> topSpeakers = chatUserAggregateService.getTopReplyTo(1L);

        // then
        assertEquals(2, topSpeakers.size());
        assertEquals("1", topSpeakers.get(0));
        assertEquals("2", topSpeakers.get(1));
        // Add more assertions based on your test data
    }

    @Test
    @Transactional
    void getTopReplyFrom_ReturnsExpectedList() {
        ChatUser c1 = new ChatUser();
        c1.setMessageCount(100);
        c1.setUserName("1");
        c1.setChatId(1L);
        ChatUser c2 = new ChatUser();
        c2.setMessageCount(50);
        c2.setUserName("2");
        c2.setChatId(1L);
        repository.save(c1);
        repository.save(c2);
        // when
        List<String> topSpeakers = chatUserAggregateService.getTopReplyFrom(1L);

        // then
        assertEquals(2, topSpeakers.size());
        assertEquals("1", topSpeakers.get(0));
        assertEquals("2", topSpeakers.get(1));
        // Add more assertions based on your test data
    }

    @Test
    @Transactional
    void getTopRating_ReturnsExpectedList() {
        ChatUser c1 = new ChatUser();
        c1.setMessageCount(100);
        c1.setUserName("1");
        c1.setChatId(1L);
        ChatUser c2 = new ChatUser();
        c2.setMessageCount(50);
        c2.setUserName("2");
        c2.setChatId(1L);
        repository.save(c1);
        repository.save(c2);
        // when
        List<String> topSpeakers = chatUserAggregateService.getTopRating(1L);

        // then
        assertEquals(2, topSpeakers.size());
        assertEquals("1", topSpeakers.get(0));
        assertEquals("2", topSpeakers.get(1));
        // Add more assertions based on your test data
    }

    @Test
    @Transactional
    void getTopUser_ReturnsExpectedList() {
        ChatUser c1 = new ChatUser();
        c1.setMessageCount(100);
        c1.setUserName("1");
        c1.setChatId(1L);
        ChatUser c2 = new ChatUser();
        c2.setMessageCount(50);
        c2.setUserName("2");
        c2.setChatId(1L);
        repository.save(c1);
        repository.save(c2);
        // when
        List<String> topSpeakers = chatUserAggregateService.getTopUser(1L);

        // then
        assertEquals(2, topSpeakers.size());
        assertEquals("1", topSpeakers.get(0));
        assertEquals("2", topSpeakers.get(1));
        // Add more assertions based on your test data
    }
}
