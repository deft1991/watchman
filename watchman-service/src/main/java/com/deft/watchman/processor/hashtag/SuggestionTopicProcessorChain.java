package com.deft.watchman.processor.hashtag;

import com.deft.watchman.service.SuggestTopicService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Sergey Golitsyn
 * created on 15.01.2024
 */

@RequiredArgsConstructor
public class SuggestionTopicProcessorChain extends HashTagProcessorChain {

    private final SuggestTopicService suggestTopicService;

    /**
     * Please, not that checkNext() call can be inserted both in the beginning
     * of this method and in the end.
     * <p>
     * This gives much more flexibility than a simple loop over all middleware
     * objects. For instance, an element of a chain can change the order of
     * checks by running its check after all other checks.
     */
    @Override
    public boolean check(Message message) {
        String text = message.getText();
        if (suggestTopicService.containsSuggestTopic(text)) {
            suggestTopicService.addSuggestTopic(text, message.getChat().getId());
        }
        return checkNext(message);
    }

}
