package io.leaderli.litool.test;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public interface MockBeanInterface<T, R> {
    default <R2> MockBeanInterface<T, R2> call(R2 call) {
        return (MockBeanInterface<T, R2>) this;
    }

    default MockBeanInterface<T, Void> run(Runnable call) {
        call.run();
        other((R) null);
        return (MockBeanInterface<T, Void>) this;
    }

    MockBeanInterface<T, R> consume(Consumer<T> call);

    default <R2> MockBeanInterface<T, R2> supplier(Supplier<R> call) {
        call.get();
        return (MockBeanInterface<T, R2>) this;
    }

    <R2> MockBeanInterface<T, R2> function(Function<T, R> call);

    MockBeanInterface<T, R> then(R value);

    MockBeanInterface<T, R> other(R value);

    MockBeanInterface<T, R> other(BiFunction<Method, Object[], R> function);

    T build();

    default <R2> MockBeanInterface<T, Void> call(R2 call, R2 result) {
        then((R) result);
        return (MockBeanInterface<T, Void>) this;
    }

    default <R2> MockBeanInterface<T, Void> call(R2 call, R2 result, R2 other) {
        then((R) result);
        other((R) other);
        return (MockBeanInterface<T, Void>) this;
    }

    default <R2> MockBeanInterface<T, Void> when(Function<T, R2> call, R2 result) {
        function((Function<T, R>) call);
        then((R) result);
        return (MockBeanInterface<T, Void>) this;
    }


    default <R2> MockBeanInterface<T, Void> when(Function<T, R2> call, R2 result, R2 other) {
        function((Function<T, R>) call);
        then((R) result);
        other((R) other);
        return (MockBeanInterface<T, Void>) this;
    }

    default <R2> MockBeanInterface<T, Void> other(Function<T, R2> call, BiFunction<Method, Object[], R2> otherValue) {
        function((Function<T, R>) call);
        other((BiFunction<Method, Object[], R>) otherValue);
        return (MockBeanInterface<T, Void>) this;
    }


    default <R2> MockBeanInterface<T, Void> other(Function<T, R2> call, R2 result, BiFunction<Method, Object[], R2> otherValue) {
        function((Function<T, R>) call);
        then((R) result);
        other((BiFunction<Method, Object[], R>) otherValue);
        return (MockBeanInterface<T, Void>) this;
    }

}




