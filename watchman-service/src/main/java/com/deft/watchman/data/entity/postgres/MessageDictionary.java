package com.deft.watchman.data.entity.postgres;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@Cache(region = "ChatMessage", usage = CacheConcurrencyStrategy.READ_WRITE)
public class MessageDictionary extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private MessageType type;
    private String message;

}
