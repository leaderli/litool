package io.leaderli.litool.core.meta.logic;

/**
 * @author leaderli
 * @since 2022/9/12
 */
@FunctionalInterface
public interface SubscriptionLogic<T> {


    /**
     * request the test value
     *
     * @param value the test value
     */
    void request(T value);


}
