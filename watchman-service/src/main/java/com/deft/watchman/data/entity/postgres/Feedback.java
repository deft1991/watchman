package com.deft.watchman.data.entity.postgres;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;

/**
 * @author Sergey Golitsyn
 * created on 24.02.2024
 * <p>
 * Entity to get data from Google Forms.
 * We cannot use BaseEntity for now. Thare are no create date field.
 */

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
public class Feedback {

    @Id
    @UuidGenerator
    private String id;
    private Long chatId;
    private String message;
    @Builder.Default
    private boolean send = false;
    private Instant updateDate;

}
