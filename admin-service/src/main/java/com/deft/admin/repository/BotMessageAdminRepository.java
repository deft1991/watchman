package com.deft.admin.repository;

import com.deft.admin.data.BotMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Sergey Golitsyn
 * created on 19.01.2024
 */
public interface BotMessageAdminRepository extends JpaRepository<BotMessageEntity, String>, JpaSpecificationExecutor<BotMessageEntity> {

}
