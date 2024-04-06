package com.deft.rss.data.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sergey Golitsyn
 * created on 30.03.2024
 */

@Getter
@Setter
public class RssItemDto {

    public static final String BOLD_DESCRIPTION = "*Description:*";
    private String title;
    private String description;
    private String link;

    /**
     * If description is longer than param
     *
     * @param descriptionMaxLength Trim it to descriptionMaxLength
     */
    private String getShortText(int descriptionMaxLength, String value) {
        if (value.length() > descriptionMaxLength) {
            return value.substring(0, descriptionMaxLength) + "...";
        } else {
            return value;
        }
    }

    public String convertToPrintString(int descriptionMaxLength) {
        return "[" + getShortText(descriptionMaxLength, this.title) + "]" +
                "(" + this.link + ")" +
                "\n" +
                BOLD_DESCRIPTION + " " +
                getShortText(descriptionMaxLength, this.description)
                        .replaceAll("<[^>]*>", "") +
                "\n";
    }
}
