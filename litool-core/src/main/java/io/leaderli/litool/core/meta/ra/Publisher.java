package io.leaderli.litool.core.meta.ra;

/**
 * A {@link Publisher} is a provider of a potentially unbounded number of sequenced elements, publishing them according to
 * the demand received from its {@link Subscriber}
 * <p>
 * A {@link Publisher} can serve multiple {@link Subscriber} subscribed {@link #subscribe(Subscriber)} dynamically
 * at various points in time
 *
 * @param <T> the type of element signaled
 * @author leaderli
 * @since 2022/6/27
 */
public interface Publisher<T> {


    /**
     * Request {@link Publisher} to start streaming data
     * <p>
     * This is a "factory method" and can be call multi times, each time starting a new  {@link  Subscription}
     * <p>
     * Each {@link Subscription} will work for only a single {@link Subscriber}
     *
     * @param subscriber the  {@link Subscriber} will consume signal from this {@link Publisher}
     */
    void subscribe(Subscriber<? super T> subscriber);
}
