package com.deft.rss.repositiory;

import com.deft.rss.data.entity.ChatRssSettings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * @author Sergey Golitsyn
 * created on 30.03.2024
 */
@Repository
public interface ChatRssSettingsRepository extends CrudRepository<ChatRssSettings, String> {
}
