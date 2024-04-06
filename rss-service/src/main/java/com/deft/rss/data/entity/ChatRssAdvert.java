package com.deft.rss.data.entity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

/**
 * @author Sergey Golitsyn
 * created on 05.04.2024
 */

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
public class ChatRssAdvert extends BaseEntity {

    private Long chatId;
    private String title;
    private String description;
    private String link;
    private Boolean published;
    private Instant scheduledDate;

}
