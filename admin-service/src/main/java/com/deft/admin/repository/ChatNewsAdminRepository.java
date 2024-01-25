package com.deft.admin.repository;

import com.deft.admin.data.ChatNewsAdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Sergey Golitsyn
 * created on 19.01.2024
 */
public interface ChatNewsAdminRepository extends JpaRepository<ChatNewsAdminEntity, String>, JpaSpecificationExecutor<ChatNewsAdminEntity> {

}
