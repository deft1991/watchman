package com.deft.watchman.data.entity.postgres;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Sergey Golitsyn
 * created on 19.01.2024
 */

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "ChatSettings", usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChatSettings extends BaseEntity {

    private Long chatId;
    private String chatName;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LanguageType chatLanguage = LanguageType.ENG;
    private boolean linkedinEnable;
    @Builder.Default
    private int banWaitTimeSeconds = 1800;

}
