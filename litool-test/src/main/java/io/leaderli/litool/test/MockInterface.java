package io.leaderli.litool.test;

import io.leaderli.litool.core.collection.ArrayEqual;
import io.leaderli.litool.core.type.PrimitiveEnum;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"unchecked", "rawtypes"})
public class MockInterface<T> extends MethodValueRecorder<MockInterface<T>> {


    public <R> MockInterface<T> when(Function<T, R> call, R result) {
        call.apply(instance);
        return record(result, null, 0b10);
    }

    public <R> MockInterface<T> when(Consumer<T> call) {
        call.accept(instance);
        return record(null, (m, arg) -> null, 0b01);
    }

    public <R> MockInterface<T> when(Function<T, R> call, BiFunction<Method, Object[], R> otherValue) {
        call.apply(instance);
        return record(null, (m, arg) -> null, 0b01);
    }

    public <R> MockInterface<T> when(Function<T, R> call, R result, R other) {
        call.apply(instance);
        return record(result, (m, args) -> other, 0b11);
    }

    public <R> MockInterface<T> when(Function<T, R> call, R result, BiFunction<Method, Object[], R> otherValue) {
        call.apply(instance);
        return record(result, otherValue, 0b11);
    }

    final T instance;

    MockInterface(Class<T> mockClass) {
        super(mockClass);
        instance = (T) Proxy.newProxyInstance(mockClass.getClassLoader(), new Class[]{mockClass}, (proxy, method, args) -> {
            currentMethod = method;
            currentArgs = ArrayEqual.of(args);
            methodValueMap.put(currentMethod, new MethodValue(currentMethod));
            return PrimitiveEnum.get(method.getReturnType()).zero_value;
        });
    }

    public T build() {
        return (T) Proxy.newProxyInstance(mockClass.getClassLoader(), new Class[]{mockClass}, (proxy, method, args) -> getMethodValue(method, args));
    }


}
