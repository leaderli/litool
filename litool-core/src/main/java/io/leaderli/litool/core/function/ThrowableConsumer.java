package io.leaderli.litool.core.function;


import java.util.function.Consumer;

/**
 * 一个声明异常标识的 {@link  Consumer}
 *
 * @param <T> 消费类型
 */
@FunctionalInterface
public interface ThrowableConsumer<T> {


    void accept(T t) throws Throwable;
}
