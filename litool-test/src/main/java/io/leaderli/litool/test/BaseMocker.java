package io.leaderli.litool.test;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.function.Filter;
import io.leaderli.litool.core.type.MethodFilter;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class BaseMocker<T, R> extends MethodValueRecorder implements IMocker<T, R> {
    protected T instance;
    protected MethodValue currentMethodValue;

    protected BaseMocker(Class<T> mockClass) {
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
    public <R2> IMocker<T, R2> function(Function<T, R2> call) {
        call.apply(instance);
        return (IMocker<T, R2>) this;
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
    public IMocker<T, R> other(MethodProxy function) {
        LiAssertUtil.assertNotNull(currentMethodValue, IllegalStateException::new);
        currentMethodValue.otherFunction(function);
        return this;
    }

    public BaseMocker<T, R> mock(MethodFilter methodFilter, MethodProxy otherFunction) {

        for (Method declaredMethod : LiMock.findDeclaredMethods(mockClass, methodFilter)) {
            methodValueMap.computeIfAbsent(declaredMethod, MethodValue::new).otherFunction(otherFunction);
        }
        return this;
    }

}
