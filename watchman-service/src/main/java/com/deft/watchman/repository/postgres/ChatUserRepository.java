package com.deft.watchman.repository.postgres;

import com.deft.watchman.data.entity.postgres.ChatUser;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
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

    Set<ChatUser> findAllByNewUserTrueAndLeaveFalse();
}
