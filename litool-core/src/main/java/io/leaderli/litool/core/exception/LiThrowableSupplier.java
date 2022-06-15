package io.leaderli.litool.core.exception;

/**
 * @author leaderli
 * @since 2022/6/16
 */
public interface LiThrowableSupplier<T> {

    T get() throws Throwable;
}
