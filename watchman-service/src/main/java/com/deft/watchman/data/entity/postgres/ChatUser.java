package com.deft.watchman.data.entity.postgres;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.Instant;

/**
 * @author Sergey Golitsyn
 * created on 05.10.2023
 */

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "defaultCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChatUser extends BaseEntity {

    private Long userId;

    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private boolean bot;
    private boolean newUser = false;
    private Integer welcomeMessageId;
    private String inviteMessage;
    private String linkedinUrl;
    private int rating;
    private boolean leave = false;
    private Instant joinGroupTime; // use it to check exp time
    private long messageCount = 0;
    private long replyToCount = 0;
    private long replyFromCount = 0;
}
