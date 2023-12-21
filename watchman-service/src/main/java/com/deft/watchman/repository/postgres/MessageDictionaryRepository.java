package com.deft.watchman.repository.postgres;

import com.deft.watchman.data.entity.postgres.MessageDictionary;
import com.deft.watchman.data.entity.postgres.MessageType;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 05.10.2023
 */
@Repository
public interface MessageDictionaryRepository extends CrudRepository<MessageDictionary, Long> {
    @QueryHints(@QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true"))
    Optional<MessageDictionary> findByType(MessageType type);
}
