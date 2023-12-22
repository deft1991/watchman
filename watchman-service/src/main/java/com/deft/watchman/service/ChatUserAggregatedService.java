package com.deft.watchman.service;

import java.util.List;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */
public interface ChatUserAggregatedService {

    List<String> getTopSpeakers();

    List<String> getTopReplyTo();

    List<String> getTopReplyFrom();

    List<String> getTopRating();

    List<String> getTopUser();
}
