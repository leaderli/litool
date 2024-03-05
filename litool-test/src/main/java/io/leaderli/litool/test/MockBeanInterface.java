package io.leaderli.litool.test;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public interface MockBeanInterface<T, R> {
    default <RR> MockBeanInterface<T, RR> call(RR call) {
        return (MockBeanInterface<T, RR>) this;
    }

    default MockBeanInterface<T, Void> run(Runnable call) {
        call.run();
        other((R) null);
        return (MockBeanInterface<T, Void>) this;
    }

    MockBeanInterface<T, R> consume(Consumer<T> call);

    default <RR> MockBeanInterface<T, RR> supplier(Supplier<R> call) {
        call.get();
        return (MockBeanInterface<T, RR>) this;
    }

    <RR> MockBeanInterface<T, RR> function(Function<T, R> call);

    MockBeanInterface<T, R> then(R value);

    MockBeanInterface<T, R> other(R value);

    MockBeanInterface<T, R> other(BiFunction<Method, Object[], R> function);

    T build();

    default <RR> MockBeanInterface<T, Void> call(RR call, RR result) {
        then((R) result);
        return (MockBeanInterface<T, Void>) this;
    }

    default <RR> MockBeanInterface<T, Void> call(RR call, RR result, RR other) {
        then((R) result);
        other((R) other);
        return (MockBeanInterface<T, Void>) this;
    }

    default <RR> MockBeanInterface<T, Void> when(Function<T, RR> call, RR result) {
        function((Function<T, R>) call);
        then((R) result);
        return (MockBeanInterface<T, Void>) this;
    }


    default <RR> MockBeanInterface<T, Void> when(Function<T, RR> call, RR result, RR other) {
        function((Function<T, R>) call);
        then((R) result);
        other((R) other);
        return (MockBeanInterface<T, Void>) this;
    }

    default <RR> MockBeanInterface<T, Void> other(Function<T, RR> call, BiFunction<Method, Object[], RR> otherValue) {
        function((Function<T, R>) call);
        other((BiFunction<Method, Object[], R>) otherValue);
        return (MockBeanInterface<T, Void>) this;
    }


    default <RR> MockBeanInterface<T, Void> other(Function<T, RR> call, RR result, BiFunction<Method, Object[], RR> otherValue) {
        function((Function<T, R>) call);
        then((R) result);
        other((BiFunction<Method, Object[], R>) otherValue);
        return (MockBeanInterface<T, Void>) this;
    }

}




