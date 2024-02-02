package com.deft.admin.repository;

import com.deft.admin.data.ChatSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Sergey Golitsyn
 * created on 28.01.2024
 */
public interface ChatSettingsAdminRepository extends JpaRepository<ChatSettingsEntity, String>, JpaSpecificationExecutor<ChatSettingsEntity> {

}
