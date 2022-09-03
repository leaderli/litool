package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/6/27
 * A {@link Subscription} represents a one-to-one lifecycle of a {@link Subscriber} subscribing to a {@link Publisher}
 * <p>
 * It can only be used once by a single {@link Subscriber}
 * <p>
 * It is used to both signal desire for data and cancel demand
 */
public interface Subscription extends CancelSubscription {

    default void request() {
        request(0);
    }

    /**
     * No events will be send by a {@link Publisher} until is signaled via this method
     */
    void request(int bit);


}
