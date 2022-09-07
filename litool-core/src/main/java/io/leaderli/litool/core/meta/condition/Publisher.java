package io.leaderli.litool.core.meta.condition;

/**
 * A {@link  Publisher} can publishing source value according to the demand from its {@link  Subscriber}
 *
 * @param <T> the type of source value
 * @param <R> the type of target value
 */
@FunctionalInterface
interface Publisher<T, R> {
    /**
     * Request {@link  Publisher} to start the chain
     * <p>
     * This is a "factory method" and can be call multi times, each time starting a new {@link  Subscription}
     * <p>
     * Each {@link  Subscription} will work only for a single {@link Subscriber}
     *
     * @param subscriber the subscriber will consume signal from {@link Publisher}
     * @see Subscriber#onSubscribe(Subscription)
     */
    void subscribe(Subscriber<? super T, R> subscriber);
}
