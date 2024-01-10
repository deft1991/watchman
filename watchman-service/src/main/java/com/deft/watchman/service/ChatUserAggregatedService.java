package com.deft.watchman.service;

import lombok.NonNull;

import java.util.List;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */
public interface ChatUserAggregatedService {

    List<String> getTopSpeakers(@NonNull Long id);

    List<String> getTopReplyTo(@NonNull Long id);

    List<String> getTopReplyFrom(@NonNull Long id);

    List<String> getTopRating(@NonNull Long id);

    List<String> getTopUser(@NonNull Long id);
}
