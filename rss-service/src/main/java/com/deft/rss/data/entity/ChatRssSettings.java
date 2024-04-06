package com.deft.rss.data.entity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Collections;
import java.util.Set;

/**
 * @author Sergey Golitsyn
 * created on 30.03.2024
 */

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
public class ChatRssSettings extends BaseEntity {

    private Long chatId;
    @Builder.Default
    private String[] rssLinks = new String[0];
    @Builder.Default
    private String[] matchWords = new String[0];

    public Set<String> rssLinsSet() {
        return Set.of(rssLinks);
    }

    public Set<String> matchWordsSet() {
        if (matchWords != null && matchWords.length > 0) {
            return Set.of(matchWords);
        }
        return Collections.emptySet();
    }

}
