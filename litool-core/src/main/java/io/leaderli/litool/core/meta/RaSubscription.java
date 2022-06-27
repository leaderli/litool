package io.leaderli.litool.core.meta;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public interface RaSubscription {

    /**
     * 请求数据
     *
     * @param n 请求 n 个数据
     */
    void request(int n);

    /**
     * 取消操作
     */
    void cancel();
}
