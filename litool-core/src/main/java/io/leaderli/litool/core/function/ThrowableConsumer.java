package io.leaderli.litool.core.function;

/**
 * @author leaderli
 * @since 2022/6/16
 */

@FunctionalInterface
public interface ThrowableConsumer<T> {

void accept(T t) throws Throwable;
}
