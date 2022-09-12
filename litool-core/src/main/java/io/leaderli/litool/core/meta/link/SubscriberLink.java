package io.leaderli.litool.core.meta.link;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public interface SubscriberLink<T> {

    void onSubscribe(SubscriptionLink subscription);

    /**
     * deliver execute to next node
     *
     * @param value the value
     */
    void next(T value);


    /**
     * called on the execution chain is interrupted
     *
     * @param value the value
     */
    default void onInterrupt(T value) {

    }

}
