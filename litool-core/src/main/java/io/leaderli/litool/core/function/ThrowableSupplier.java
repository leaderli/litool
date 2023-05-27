package io.leaderli.litool.core.function;

import java.util.function.Supplier;

/**
 * 一个声明异常标识的 {@link  Supplier}
 *
 * @param <T> 提供者的类型
 * @author leaderli
 * @since 2022/6/16
 */


@FunctionalInterface
public interface ThrowableSupplier<T> {
    /**
     * Gets a result.
     *
     * @return a result
     * @throws Throwable error
     */
    T get() throws Throwable;
}
