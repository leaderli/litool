package io.leaderli.litool.core.meta.reactor;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public interface LinkSubscription<T> {

    /**
     * 请求数据
     *
     * @param t 数据
     */
    void request(T t);

}
