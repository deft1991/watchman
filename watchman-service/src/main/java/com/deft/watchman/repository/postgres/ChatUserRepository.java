package com.deft.watchman.repository.postgres;

import com.deft.watchman.data.entity.postgres.ChatUser;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

/**
 * @author Sergey Golitsyn
 * created on 05.10.2023
 *
 *
 */
@Repository
public interface ChatUserRepository extends CrudRepository<ChatUser, Long> {
    @QueryHints(@QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true"))
    Optional<ChatUser> findByUserIdAndChatId(Long userId, Long chatId);

    @QueryHints(@QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true"))
    Optional<ChatUser> findByUserNameAndChatId(String username, Long chatId);

    Set<ChatUser> findAllByNewUserTrueAndLeaveFalseAndJoinGroupTimeIsBefore(Instant time);

    Set<ChatUser> findTop5ByChatIdOrderByMessageCountDesc(Long chatId);

    Set<ChatUser> findTop5ByChatIdOrderByReplyToCountDesc(Long chatId);

    Set<ChatUser> findTop5ByChatIdOrderByReplyFromCountDesc(Long chatId);

    Set<ChatUser> findTop5ByChatIdOrderByRatingDesc(Long chatId);

    @Query("select c from ChatUser c where c.chatId = :chatId order by (c.messageCount + c.replyToCount + c.replyFromCount + c.rating) desc")
    Set<ChatUser> findTop5Users(@Param("chatId") Long chatId);
}
