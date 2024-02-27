package io.leaderli.litool.test;

import io.leaderli.litool.core.collection.ArrayEqual;
import io.leaderli.litool.core.meta.Either;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class MethodValueRecorder<T> {

    protected final Class<?> mockClass;
    protected final Map<Method, MethodValue> methodValueMap = new HashMap<>();

    protected Method currentMethod;
    protected ArrayEqual currentArgs;


    public MethodValueRecorder(Class<?> mockClass) {
        this.mockClass = mockClass;
    }

    protected <R> T record(R result, BiFunction<Method, Object[], R> otherValue, int state) {
        if (currentMethod == null) {
            throw new IllegalStateException("method may abstract, it's not support");
        }
        if ((state & 0b10) == 0b10) {
            methodValueMap.get(currentMethod).whenValue.put(currentArgs, result);
        }
        if ((state & 0b01) == 0b01) {
            methodValueMap.get(currentMethod).otherValue = otherValue;
        }
        currentMethod = null;
        currentArgs = null;
        return (T) this;
    }

    protected Object getMethodValue(Method m, Object[] args) {
        MethodValue methodValue = methodValueMap.get(m);
        ArrayEqual<Object> key = ArrayEqual.of(args);
        if (methodValue.whenValue.containsKey(key)) {
            return methodValue.whenValue.get(key);
        }
        if (methodValue.otherValue == null) {
            return Either.none();
        }
        return methodValue.otherValue.apply(m, args);
    }
}
