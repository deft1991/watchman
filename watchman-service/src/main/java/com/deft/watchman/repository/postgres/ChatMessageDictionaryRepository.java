package com.deft.watchman.repository.postgres;

import com.deft.watchman.data.entity.postgres.ChatMessageDictionary;
import com.deft.watchman.data.entity.postgres.LanguageType;
import com.deft.watchman.data.entity.postgres.MessageType;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 08.02.2024
 */
@Repository
public interface ChatMessageDictionaryRepository extends CrudRepository<ChatMessageDictionary, Long> {

    @QueryHints(@QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true"))
    Optional<ChatMessageDictionary> findByChatIdAndTypeAndLanguage(Long chatId, MessageType type, LanguageType languageType);

    @QueryHints(@QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true"))
    List<ChatMessageDictionary> findByChatId(Long chatId);
}
