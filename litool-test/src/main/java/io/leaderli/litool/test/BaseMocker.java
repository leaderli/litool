package io.leaderli.litool.test;

import io.leaderli.litool.core.exception.LiAssertUtil;

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
    public <RR> MockBeanInterface<T, RR> function(Function<T, R> call) {
        call.apply(instance);
        return (MockBeanInterface<T, RR>) this;
    }

    @SuppressWarnings("unchecked")
    public MockBeanInterface<T, R> then(R value) {
        LiAssertUtil.assertNotNull(currentMethodValue, IllegalStateException::new);
        currentMethodValue.then(value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public MockBeanInterface<T, R> other(R value) {
        LiAssertUtil.assertNotNull(currentMethodValue, IllegalStateException::new);
        currentMethodValue.other(value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public MockBeanInterface<T, R> other(BiFunction<Method, Object[], R> function) {
        LiAssertUtil.assertNotNull(currentMethodValue, IllegalStateException::new);
        currentMethodValue.other(function);
        return this;
    }
}
