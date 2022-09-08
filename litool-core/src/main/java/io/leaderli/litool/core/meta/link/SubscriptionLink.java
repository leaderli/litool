package io.leaderli.litool.core.meta.link;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public interface SubscriptionLink<T> {

    /**
     * 请求数据
     */
    void request();

    /**
     * 请求数据
     *
     * @param t 新的实例
     */
    void request(T t);
}
