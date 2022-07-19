package io.leaderli.litool.core.exception;

/**
 * @author leaderli
 * @since 2022/6/16
 */

@FunctionalInterface
public interface LiThrowableConsumer<T> {

    void accept(T t) throws Throwable;
}
