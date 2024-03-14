package io.leaderli.litool.test;

import io.leaderli.litool.core.type.MethodFilter;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public interface IMocker<T, R> {
    default <RR> IMocker<T, RR> call(RR call) {
        return (IMocker<T, RR>) this;
    }

    default IMocker<T, Void> run(Runnable call) {
        call.run();
        other((R) null);
        return (IMocker<T, Void>) this;
    }

    IMocker<T, R> consume(Consumer<T> call);

    default <RR> IMocker<T, RR> supplier(Supplier<R> call) {
        call.get();
        return (IMocker<T, RR>) this;
    }

    <RR> IMocker<T, RR> function(Function<T, RR> call);

    IMocker<T, R> then(R value);

    IMocker<T, R> other(R value);

    IMocker<T, R> other(MethodProxy<R> function);

    default IMocker<T, R> arg0(Object arg) {
        return argx(0, arg);
    }

    default IMocker<T, R> arg1(Object arg) {
        return argx(1, arg);
    }

    default IMocker<T, R> arg2(Object arg) {
        return argx(2, arg);
    }

    default IMocker<T, R> arg3(Object arg) {
        return argx(3, arg);
    }

    IMocker<T, R> argx(int x, Object arg);


    default <RR> IMocker<T, Void> call(RR call, RR result) {
        then((R) result);
        return (IMocker<T, Void>) this;
    }

    default <RR> IMocker<T, Void> call(RR call, RR result, RR other) {
        then((R) result);
        other((R) other);
        return (IMocker<T, Void>) this;
    }

    default <RR> IMocker<T, Void> when(Function<T, RR> call, RR result) {
        function((Function<T, R>) call);
        then((R) result);
        return (IMocker<T, Void>) this;
    }


    default <RR> IMocker<T, Void> when(Function<T, RR> call, RR result, RR other) {
        function((Function<T, R>) call);
        then((R) result);
        other((R) other);
        return (IMocker<T, Void>) this;
    }

    default <RR> IMocker<T, Void> other(Function<T, RR> call, MethodProxy<RR> otherValue) {
        function((Function<T, R>) call);
        other((MethodProxy<R>) otherValue);
        return (IMocker<T, Void>) this;
    }


    default <RR> IMocker<T, Void> other(Function<T, RR> call, RR result, MethodProxy<RR> otherValue) {
        function((Function<T, R>) call);
        then((R) result);
        other((MethodProxy<R>) otherValue);
        return (IMocker<T, Void>) this;
    }

    IMocker<T, R> mock(MethodFilter methodFilter, MethodProxy<R> methodProxy);

    T build();
}




