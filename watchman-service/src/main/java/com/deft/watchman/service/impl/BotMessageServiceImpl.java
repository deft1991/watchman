package com.deft.watchman.service.impl;

import com.deft.watchman.data.entity.postgres.BotMessage;
import com.deft.watchman.repository.postgres.BotMessageRepository;
import com.deft.watchman.service.BotMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sergey Golitsyn
 * created on 26.01.2024
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class BotMessageServiceImpl implements BotMessageService {

    private final BotMessageRepository botMessageRepository;

    @Override
    public List<Triple<Long, String, String>> getMessagesToSend() {
        List<BotMessage> news = botMessageRepository.findAllForSend();

        return news.parallelStream()
                .map(message -> Triple.of(message.getChatId(), message.getMessage(), message.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void markAsSent(List<String> sentIds) {
        botMessageRepository.markAsSent(sentIds);
    }

}
