package io.leaderli.litool.core.exception;

/**
 * @author leaderli
 * @since 2022/6/16
 */
public interface LiThrowableFunction<T,R> {


    R apply(T t);

}
