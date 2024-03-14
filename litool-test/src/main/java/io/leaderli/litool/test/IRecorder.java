package io.leaderli.litool.test;

import io.leaderli.litool.core.type.MethodFilter;

import java.util.function.Consumer;
import java.util.function.Function;

public interface IRecorder<B, T> {

    @SuppressWarnings("unchecked")
    default B call(T call) {
        return (B) this;
    }

    B consume(Consumer<T> call);

    B function(Function<T, Object> call);

    B called();

    B arg(int index, Object arg);

    B args(Object... args);

    B assertReturn(Object compareReturn);

    B record(MethodFilter methodFilter, MethodAssert methodAssert);

    T build();
}

