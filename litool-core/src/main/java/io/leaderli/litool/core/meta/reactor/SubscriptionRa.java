package io.leaderli.litool.core.meta.reactor;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public interface SubscriptionRa extends CancelSubscriptionRa {

    /**
     * 请求数据
     *
     * @param n 请求 n 个数据
     */
    void request(int n);

}
