package com.deft.admin.data;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Sergey Golitsyn
 * created on 05.10.2023
 */

@Entity
@Table(name = "chat_news")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "ChatNews", usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChatNewsAdminEntity extends BaseEntity {

    private Long chatId;
    private String newsText;

}
