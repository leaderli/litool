package io.leaderli.litool.test;

import io.leaderli.litool.core.exception.LiAssertUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class MockBeans<T> implements MockBeanInterface<MockBeans<T>, T> {

    private final List<MockBean<T>> mockBeans = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public MockBeans(Class<? extends T>... mockClasses) {
        for (Class<? extends T> mockClass : mockClasses) {
            mockBeans.add(new MockBean<>((Class<T>) mockClass, true));
        }
    }

    @Override
    public MockBeans<T> consume(Consumer<T> call) {
        mockBeans.forEach(mockBean -> mockBean.consume(call));
        return this;
    }

    public <R> MockBeans<T> when(Function<T, R> call, R result) {
        mockBeans.forEach(mockBean -> mockBean.when(call, result));
        return this;
    }

    public <R> MockBeans<T> when(Function<T, R> call, R result, R other) {
        mockBeans.forEach(mockBean -> mockBean.when(call, result, other));
        return this;
    }

    public <R> MockBeans<T> other(Function<T, R> call, BiFunction<Method, Object[], R> otherValue) {
        mockBeans.forEach(mockBean -> mockBean.other(call, otherValue));
        return this;
    }


    public <R> MockBeans<T> other(Function<T, R> call, R result, BiFunction<Method, Object[], R> otherValue) {
        mockBeans.forEach(mockBean -> mockBean.other(call, result, otherValue));
        return this;
    }


    public T build() {
        T instance = null;
        for (MockBean<T> mockBean : mockBeans) {
            instance = mockBean.build();
        }
        LiAssertUtil.assertFalse(instance == null, new IllegalArgumentException());
        return instance;
    }
}
