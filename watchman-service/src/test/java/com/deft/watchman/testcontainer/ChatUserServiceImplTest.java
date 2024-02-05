package com.deft.watchman.testcontainer;

import com.deft.watchman.WatchmanApplication;
import com.deft.watchman.bot.WatchmanBot;
import com.deft.watchman.data.entity.postgres.ChatUser;
import com.deft.watchman.repository.postgres.ChatUserRepository;
import com.deft.watchman.service.impl.ChatUserServiceImpl;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
import org.telegram.telegrambots.meta.api.objects.User;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * @author Sergey Golitsyn
 * created on 05.02.2024
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WatchmanApplication.class)
@ActiveProfiles("test")
@ContextConfiguration(initializers = {ChatUserServiceImplTest.Initializer.class})
public class ChatUserServiceImplTest {

    @MockBean
    private WatchmanBot watchmanBot;

    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
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
    private ChatUserServiceImpl chatUserService;

    @Autowired
    private ChatUserRepository chatUserRepository;

    @ParameterizedTest
    @Transactional
    @CsvSource({"1,1,true", "2,1,true", "3,2,false"})
    void findByUserIdAndChatId(Long userId, Long chatId, boolean expectedResult) {
        // Given
        ChatUser user = new ChatUser();
        user.setChatId(chatId);
        user.setUserId(userId);
        chatUserRepository.save(user);

        // when
        Optional<ChatUser> result = chatUserService.findByUserIdAndChatId(userId, 1L);

        // then
        if (expectedResult) {
            assertTrue(result.isPresent());
            assertSame(user, result.get());
        } else {
            assertTrue(result.isEmpty());
        }
    }

    @ParameterizedTest
    @Transactional
    @CsvSource({"1,1,true", "2,1,true", "3,2,false"})
    void findByUserNameAndChatId(String userName, Long chatId, boolean expectedResult) {
        // Given
        ChatUser user = new ChatUser();
        user.setChatId(chatId);
        user.setUserName(userName);
        chatUserRepository.save(user);

        // when
        Optional<ChatUser> result = chatUserService.findByUserNameAndChatId(userName, 1L);

        // then
        if (expectedResult) {
            assertTrue(result.isPresent());
            assertSame(user, result.get());
        } else {
            assertTrue(result.isEmpty());
        }
    }

    @ParameterizedTest
    @Transactional
    @CsvSource({"1,1,false", "2,1,false", "3,2,true"})
    void createOldUser(Long userId, Long chatId, boolean isBot) {
        // Given
        User user = new User();
        user.setIsBot(isBot);
        user.setId(userId);

        // when
        ChatUser oldUser = chatUserService.createOldUser(user, chatId);

        // then
        assertNotNull(oldUser);
        assertEquals(userId, oldUser.getUserId());
        assertEquals(chatId, oldUser.getChatId());
        assertFalse(oldUser.isNewUser());
        assertFalse(oldUser.isLeave());
        assertEquals(isBot, oldUser.isBot());
    }

    @ParameterizedTest
    @Transactional
    @CsvSource({"1,1,1", "2,1,2", "3,2,123"})
    void increaseMessageCount(Long userId, Long chatId, int messageCount) {
        // Given
        ChatUser user = new ChatUser();
        user.setChatId(chatId);
        user.setUserId(userId);
        user.setMessageCount(messageCount);
        chatUserRepository.save(user);

        // when
        chatUserService.increaseMessageCount(userId, chatId);
        Optional<ChatUser> userOptional = chatUserRepository.findByUserIdAndChatId(userId, chatId);

        // then
        assertTrue(userOptional.isPresent());
        user = userOptional.get();
        assertEquals(userId, user.getUserId());
        assertEquals(chatId, user.getChatId());
        assertEquals(messageCount + 1, user.getMessageCount());
    }

    @ParameterizedTest
    @Transactional
    @CsvSource({"1,1,1", "2,1,2", "3,2,123"})
    void increaseReplyToCount(Long userId, Long chatId, int messageCount) {
        // Given
        ChatUser user = new ChatUser();
        user.setChatId(chatId);
        user.setUserId(userId);
        user.setReplyToCount(messageCount);
        chatUserRepository.save(user);

        // when
        chatUserService.increaseReplyToCount(userId, chatId);
        Optional<ChatUser> userOptional = chatUserRepository.findByUserIdAndChatId(userId, chatId);

        // then
        assertTrue(userOptional.isPresent());
        user = userOptional.get();
        assertEquals(userId, user.getUserId());
        assertEquals(chatId, user.getChatId());
        assertEquals(messageCount + 1, user.getReplyToCount());
    }

    @ParameterizedTest
    @Transactional
    @CsvSource({"1,1,1", "2,1,2", "3,2,123"})
    void increaseReplyFromCount(Long userId, Long chatId, int messageCount) {
        // Given
        ChatUser user = new ChatUser();
        user.setChatId(chatId);
        user.setUserId(userId);
        user.setReplyFromCount(messageCount);
        chatUserRepository.save(user);

        // when
        chatUserService.increaseReplyFromCount(userId, chatId);
        Optional<ChatUser> userOptional = chatUserRepository.findByUserIdAndChatId(userId, chatId);

        // then
        assertTrue(userOptional.isPresent());
        user = userOptional.get();
        assertEquals(userId, user.getUserId());
        assertEquals(chatId, user.getChatId());
        assertEquals(messageCount + 1, user.getReplyFromCount());
    }
}
