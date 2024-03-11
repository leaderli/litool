package io.leaderli.litool.test;

import io.leaderli.litool.core.exception.LiAssertUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class MockBeans<T, R> implements IMocker<T, R> {

    private final List<MockBean<T, R>> mockBeans = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public MockBeans(Class<? extends T>... mockClasses) {
        for (Class<? extends T> mockClass : mockClasses) {
            mockBeans.add(new MockBean<>((Class<T>) mockClass, true));
        }
    }

    public IMocker<T, R> consume(Consumer<T> call) {
        mockBeans.forEach(mockBean -> mockBean.consume(call));
        return this;
    }

    @Override
    public <RR> IMocker<T, RR> function(Function<T, RR> call) {
        mockBeans.forEach(mockBean -> mockBean.function(call));
        return (IMocker<T, RR>) this;
    }

    public IMocker<T, R> then(R value) {
        mockBeans.forEach(mockBean -> mockBean.then(value));
        return this;
    }

    public IMocker<T, R> other(R value) {
        mockBeans.forEach(mockBean -> mockBean.other(value));
        return this;
    }

    public IMocker<T, R> other(BiFunction<Method, Object[], R> function) {
        mockBeans.forEach(mockBean -> mockBean.other(function));
        return this;
    }

    @Override
    public IMocker<T, R> argx(int x, Object arg) {
        mockBeans.forEach(mockBean -> mockBean.argx(x, arg));
        return this;
    }


    public T build() {
        T instance = null;
        for (MockBean<T, R> mockBean : mockBeans) {
            instance = mockBean.build();
        }
        LiAssertUtil.assertFalse(instance == null, new IllegalArgumentException());
        return instance;
    }
}
