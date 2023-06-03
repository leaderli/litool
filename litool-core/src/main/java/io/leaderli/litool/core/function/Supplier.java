package io.leaderli.litool.core.function;

/**
 * 一个声明异常标识的 {@link  java.util.function.Supplier}
 *
 * @param <T> 提供者的类型
 * @author leaderli
 * @since 2022/6/16
 */


@FunctionalInterface
public interface Supplier<T> {
    /**
     * Gets a result.
     *
     * @return a result
     * @throws Throwable error
     */
    T get() throws Throwable;
}
