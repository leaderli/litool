package io.leaderli.litool.test;

import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.PrimitiveEnum;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.function.BiFunction;

@SuppressWarnings({"unchecked"})
public class MockInterface<T> extends BaseMocker<T, Object> {


    private Type type;
    private BiFunction<Method, Object[], Object> otherInvocationHandler;

    MockInterface(LiTypeToken<T> liTypeToken) {
        this((Class<T>) liTypeToken.getRawType());
        this.type = liTypeToken;
    }

    MockInterface(Class<T> mockClass) {
        super(mockClass);
        this.type = mockClass;
        InvocationHandler invocationHandler = (proxy, method, args) -> {
            currentMethodValue = methodValueMap.computeIfAbsent(method, MethodValue::new);
            currentMethodValue.args(args);
            return PrimitiveEnum.get(method.getReturnType()).zero_value;
        };
        instance = (T) Proxy.newProxyInstance(mockClass.getClassLoader(), new Class[]{mockClass}, invocationHandler);
    }

    public MockInterface<T> otherMethod(BiFunction<Method, Object[], Object> otherInvocationHandler) {
        this.otherInvocationHandler = otherInvocationHandler;
        return this;
    }


    public T build() {
        return (T) Proxy.newProxyInstance(mockClass.getClassLoader(), new Class[]{mockClass}, (proxy, method, args) -> getMethodValueOfInterface(type, method, args, otherInvocationHandler));
    }


}
