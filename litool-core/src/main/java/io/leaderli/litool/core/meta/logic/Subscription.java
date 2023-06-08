package io.leaderli.litool.core.meta.logic;

/**
 * @param <T> 测试值类型
 * @author leaderli
 * @since 2022/9/12
 */
@FunctionalInterface
public interface Subscription<T> {


    /**
     * 请求测试值
     *
     * @param value 测试值
     */
    void request(T value);


}
