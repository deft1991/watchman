package com.deft.watchman.repository.postgres;

import com.deft.watchman.data.entity.postgres.BotMessage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Sergey Golitsyn
 * created on 26.01.2024
 */
@Repository
public interface BotMessageRepository extends CrudRepository<BotMessage, String> {

    @Query(value = """ 
            select bm from BotMessage bm
            where
                (bm.send = false or bm.send is null)
                and bm.scheduledSendTime < now()
              """
    )
    List<BotMessage> findAllForSend();

    @Modifying
    @Transactional
    @Query(value = """ 
            UPDATE BotMessage bm
            SET bm.send = true
            WHERE bm.id in (:ids)
              """
    )
    void markAsSent(@Param("ids") List<String> ids);
}
