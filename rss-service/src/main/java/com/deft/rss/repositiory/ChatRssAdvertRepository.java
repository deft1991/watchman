package com.deft.rss.repositiory;

import com.deft.rss.data.entity.ChatRssAdvert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;


/**
 * @author Sergey Golitsyn
 * created on 05.04.2024
 */
@Repository
public interface ChatRssAdvertRepository extends CrudRepository<ChatRssAdvert, String> {

    Page<ChatRssAdvert> findByChatIdAndPublishedFalseAndScheduledDateBefore(Long chatId, Instant now, Pageable pageable);

    @Query("SELECT c FROM ChatRssAdvert c " +
            "WHERE c.chatId = :chatId " +
            "AND c.published = false " +
            "AND (c.scheduledDate < :now or c.scheduledDate is null)")
    Page<ChatRssAdvert> getAvailableAdvertsForChat(@Param("chatId") Long chatId,
                                                   @Param("now") Instant now,
                                                   Pageable pageable);
}
