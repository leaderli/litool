package io.leaderli.litool.test;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class BaseMocker<T, R> extends MethodValueRecorder implements MockBeanInterface<T, R> {
    protected T instance;
    protected MethodValue currentMethodValue;

    public BaseMocker(Class<T> mockClass) {
        super(mockClass);
    }

    public MockBeanInterface<T, R> consume(Consumer<T> call) {
        call.accept(instance);
        return this;
    }

    @Override
    public <R2> MockBeanInterface<T, R2> function(Function<T, R> call) {
        call.apply(instance);
        return (MockBeanInterface<T, R2>) this;
    }

    @SuppressWarnings("unchecked")
    public MockBeanInterface<T, R> then(R value) {
        currentMethodValue.then(value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public MockBeanInterface<T, R> other(R value) {
        currentMethodValue.other(value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public MockBeanInterface<T, R> other(BiFunction<Method, Object[], R> function) {
        currentMethodValue.other(function);
        return this;
    }
}
