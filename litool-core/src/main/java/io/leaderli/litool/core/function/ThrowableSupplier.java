package io.leaderli.litool.core.function;

/**
 * @author leaderli
 * @since 2022/6/16
 */

@FunctionalInterface
public interface ThrowableSupplier<T> {

    T get() throws Throwable;
}
