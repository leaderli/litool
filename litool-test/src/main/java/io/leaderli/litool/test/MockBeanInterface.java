package io.leaderli.litool.test;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public interface MockBeanInterface<B, T> {

    B consume(Consumer<T> call);

    <R> B when(Function<T, R> call, R result);

    <R> B when(Function<T, R> call, R result, R other);

    <R> B other(Function<T, R> call, BiFunction<Method, Object[], R> otherValue);

    <R> B other(Function<T, R> call, R result, BiFunction<Method, Object[], R> otherValue);

    T build();
}

