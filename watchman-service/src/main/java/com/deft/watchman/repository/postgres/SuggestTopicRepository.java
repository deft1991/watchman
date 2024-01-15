package com.deft.watchman.repository.postgres;

import com.deft.watchman.data.entity.postgres.SuggestTopic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sergey Golitsyn
 * created on 15.01.2024
 */
@Repository
public interface SuggestTopicRepository extends CrudRepository<SuggestTopic, Long> {
}
