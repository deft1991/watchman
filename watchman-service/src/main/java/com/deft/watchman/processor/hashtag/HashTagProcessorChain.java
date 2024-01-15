package com.deft.watchman.processor.hashtag;

import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Sergey Golitsyn
 * created on 15.01.2024
 */
public abstract class HashTagProcessorChain {

    private HashTagProcessorChain next;

    /**
     * Builds chains of middleware objects.
     */
    public static HashTagProcessorChain link(HashTagProcessorChain first, HashTagProcessorChain... chain) {
        HashTagProcessorChain head = first;
        for (HashTagProcessorChain nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    /**
     * Subclasses will implement this method with concrete checks.
     */
    public abstract boolean check(Message message);

    /**
     * Runs check on the next object in chain or ends traversing if we're in
     * last object in chain.
     */
    protected boolean checkNext(Message message) {
        if (next == null) {
            return true;
        }
        return next.check(message);
    }
}
