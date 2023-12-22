package com.deft.watchman.repository.postgres;

import com.deft.watchman.data.entity.postgres.ChatUser;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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

    Optional<ChatUser> findByUserNameAndChatId(String username, Long chatId);

    Set<ChatUser> findAllByNewUserTrueAndLeaveFalse();

    Set<ChatUser> findTop5ByOrderByMessageCountDesc();

    Set<ChatUser> findTop5ByOrderByReplyToCountDesc();

    Set<ChatUser> findTop5ByOrderByReplyFromCountDesc();

    Set<ChatUser> findTop5ByOrderByRatingDesc();

    @Query("select c from ChatUser c order by (c.messageCount + c.replyToCount + c.replyFromCount + c.rating) desc")
    Set<ChatUser> findTop5Users();
}
