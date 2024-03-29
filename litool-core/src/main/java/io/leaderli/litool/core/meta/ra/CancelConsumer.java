package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/9/1
 */
@FunctionalInterface
public interface CancelConsumer<T> {

    void accept(T t, CancelSubscription cancelSubscription);
}
