package com.deft.watchman.repository.postgres;

import com.deft.watchman.data.entity.postgres.ChatSettings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 19.01.2024
 */
@Repository
public interface ChatSettingsRepository extends CrudRepository<ChatSettings, Long> {
    Optional<ChatSettings> findByChatId(Long chatId);
}
