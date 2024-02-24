package com.deft.watchman.repository.postgres;

import com.deft.watchman.data.entity.postgres.Feedback;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 26.01.2024
 */
@Repository
public interface FeedbackRepository extends CrudRepository<Feedback, String> {

    Optional<Feedback> findFirstBySendFalseAndChatIdOrderByUpdateDateAsc(Long chatId);
}
