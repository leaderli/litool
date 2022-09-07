package io.leaderli.litool.core.meta.condition;

/**
 * A {@link  Subscription} represent a one-to-one lifecycle of a {@link  Subscriber} subscribing
 * to a {@link  Publisher}
 * <p>
 * It can only be used once by a single {@link  Subscriber}
 */
@FunctionalInterface
interface Subscription {

    /**
     * No events will be send by a {@link  Publisher} util signaled via this method
     */
    void request();


}
