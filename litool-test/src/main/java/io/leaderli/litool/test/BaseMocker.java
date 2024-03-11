package io.leaderli.litool.test;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.function.Filter;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class BaseMocker<T, R> extends MethodValueRecorder implements IMocker<T, R> {
    protected T instance;
    protected MethodValue currentMethodValue;

    public BaseMocker(Class<T> mockClass) {
        super(mockClass);
    }

    public IMocker<T, R> consume(Consumer<T> call) {
        call.accept(instance);
        return this;
    }

    @Override
    public IMocker<T, R> argx(int x, Object arg) {
        LiAssertUtil.assertNotNull(currentMethodValue, IllegalStateException::new);
        currentMethodValue.argsFilter((Filter<Object[]>) args -> args.length > x && Objects.equals(arg, args[x]));
        return this;
    }

    @Override
    public <RR> IMocker<T, RR> function(Function<T, RR> call) {
        call.apply(instance);
        return (IMocker<T, RR>) this;
    }

    @SuppressWarnings("unchecked")
    public IMocker<T, R> then(R value) {
        LiAssertUtil.assertNotNull(currentMethodValue, IllegalStateException::new);
        currentMethodValue.then(value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public IMocker<T, R> other(R value) {
        LiAssertUtil.assertNotNull(currentMethodValue, IllegalStateException::new);
        currentMethodValue.other(value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public IMocker<T, R> other(BiFunction<Method, Object[], R> function) {
        LiAssertUtil.assertNotNull(currentMethodValue, IllegalStateException::new);
        currentMethodValue.other(function);
        return this;
    }
}
