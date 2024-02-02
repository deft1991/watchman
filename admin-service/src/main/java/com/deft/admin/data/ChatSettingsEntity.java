package com.deft.admin.data;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Sergey Golitsyn
 * created on 19.01.2024
 */

@Entity
@Table(name = "chat_settings")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "ChatSettings", usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChatSettingsEntity extends BaseEntity {

    private Long chatId;
    private String chatName;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LanguageType chatLanguage = LanguageType.ENG;
    private boolean linkedinEnable;
    @Builder.Default
    private int banWaitTimeSeconds = 1800;

}
