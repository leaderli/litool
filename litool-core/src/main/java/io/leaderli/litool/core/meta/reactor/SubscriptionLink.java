package io.leaderli.litool.core.meta.reactor;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public interface SubscriptionLink<T> {

    /**
     * 请求数据
     */
    void request();

}
