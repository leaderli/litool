package io.leaderli.litool.core.function;


/**
 * 一个声明异常标识的 {@link  java.util.function.Consumer}
 *
 * @param <T> 消费类型
 */
@FunctionalInterface
public interface ThrowableConsumer<T> {


    void accept(T t) throws Throwable;
}
