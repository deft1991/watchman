package com.deft.admin.data;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

/**
 * @author Sergey Golitsyn
 * created on 05.10.2023
 */

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
public class BotMessageEntity extends BaseEntity {

    private Long chatId;
    private String message;
    private Instant scheduledSendTime;
    @Builder.Default
    private boolean send = false;
}
