package io.leaderli.litool.test;

import io.leaderli.litool.core.type.BeanCreator;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class MockBean<T> extends AbstractMocker<MockBean<T>> {

    final T instance;

    MockBean(Class<T> mockClass, boolean detach) {
        super(mockClass, detach);
        instance = BeanCreator.mockBean(mockClass);
    }


    public <R> MockBean<T> when(Function<T, R> call, R result) {
        call.apply(instance);
        return record(result, null, 0b10);
    }

    public <R> MockBean<T> when(Consumer<T> call) {
        call.accept(instance);
        return record(null, (m, arg) -> null, 0b01);
    }

    public <R> MockBean<T> when(Function<T, R> call, BiFunction<Method, Object[], R> otherValue) {
        call.apply(instance);
        return record(null, (m, arg) -> null, 0b01);
    }

    public <R> MockBean<T> when(Function<T, R> call, R result, R other) {
        call.apply(instance);
        return record(result, (m, args) -> other, 0b11);
    }

    public <R> MockBean<T> when(Function<T, R> call, R result, BiFunction<Method, Object[], R> otherValue) {
        call.apply(instance);
        return record(result, otherValue, 0b11);
    }


}
