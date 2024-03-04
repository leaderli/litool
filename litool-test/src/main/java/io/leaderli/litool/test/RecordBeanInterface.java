package io.leaderli.litool.test;

import java.util.function.Consumer;
import java.util.function.Function;

public interface RecordBeanInterface<B, T> {

    B consume(Consumer<T> call);

    B when(Function<T, Object> call);

    B called();

    B arg(int index, Object arg);

    B args(Object... args);

    B assertReturn(Object compareReturn);

    T build();
}

