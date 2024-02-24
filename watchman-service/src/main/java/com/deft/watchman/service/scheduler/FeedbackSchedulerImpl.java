package com.deft.watchman.service.scheduler;

import com.deft.watchman.bot.WatchmanBot;
import com.deft.watchman.data.entity.postgres.Feedback;
import com.deft.watchman.repository.postgres.ChatSettingsRepository;
import com.deft.watchman.repository.postgres.FeedbackRepository;
import com.deft.watchman.service.FeedbackScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Sergey Golitsyn
 * created on 22.12.2023
 */

@Slf4j
@Service
@EnableAsync
@Transactional
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "app.scheduling.enable", havingValue = "true", matchIfMissing = true
)
public class FeedbackSchedulerImpl implements FeedbackScheduler {

    // todo add it as template message
    public static final String FEEDBACK_TEMPLATE = """
            📢 На связи Суровый Вахтер. 📢
            
            Давайте дружно поблагодарим участника группы, который анонимно пошарил свой опыт/фидбэк с нами! 👏🫶
            -------------------------------
            
            """
            + "%s"
            + """
                 
                 
            -------------------------------
            Огромное спасибо что поделился! ❤️
                                    
            #feedback
            """;
    private final FeedbackRepository feedbackRepository;
    private final ChatSettingsRepository chatSettingsRepository;
    private final WatchmanBot watchmanBot;

    @Override
    @Async
    @Scheduled(fixedDelayString = "${scheduler.feedback.fixed-rate.in.milliseconds:600000}")
    public void sendFeedback() {
        Optional<List<Long>> allChatIds = chatSettingsRepository.findAllChatIds();

        allChatIds.orElse(new ArrayList<>()).forEach(chatId -> {
            Optional<Feedback> feedbackOptional = feedbackRepository.findFirstBySendFalseAndChatIdOrderByUpdateDateAsc(chatId);

            if (feedbackOptional.isPresent()) {
                Feedback feedback = feedbackOptional.get();
                feedback.setSend(true);
                log.info("Sending feedback to chat: {}, message: {}", chatId, feedback.getMessage());

                String message = String.format(FEEDBACK_TEMPLATE, feedback.getMessage());
                SendMessage inviteMessage = SendMessage.builder()
                        .chatId(String.valueOf(chatId))
                        .text(message)
                        .build();
                watchmanBot.silent().execute(inviteMessage);
            }
        });
    }
}
