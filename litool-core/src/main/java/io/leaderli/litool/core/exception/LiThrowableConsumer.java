package io.leaderli.litool.core.exception;

/**
 * @author leaderli
 * @since 2022/6/16
 */
public interface LiThrowableConsumer<T> {

    void accept(T t);
}
