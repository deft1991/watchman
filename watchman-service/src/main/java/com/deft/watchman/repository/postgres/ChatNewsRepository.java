package com.deft.watchman.repository.postgres;

import com.deft.watchman.data.entity.postgres.ChatNews;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * @author Sergey Golitsyn
 * created on 05.10.2023
 */
@Repository
public interface ChatNewsRepository extends CrudRepository<ChatNews, Long> {
    List<ChatNews> findAllByCreateDateIsAfter(Instant createdAt);
}
