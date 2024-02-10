package com.deft.watchman.data.entity.postgres;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Sergey Golitsyn
 * created on 08.02.2024
 */

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "ChatMessageDictionary", usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChatMessageDictionary extends BaseEntity {

    private Long chatId;
    @Enumerated(EnumType.STRING)
    private MessageType type;
    private String message;
    @Enumerated(EnumType.STRING)
    private LanguageType language;

}
