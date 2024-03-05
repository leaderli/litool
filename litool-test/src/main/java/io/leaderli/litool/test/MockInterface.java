package io.leaderli.litool.test;

import io.leaderli.litool.core.type.PrimitiveEnum;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

@SuppressWarnings({"unchecked"})
public class MockInterface<T> extends BaseMocker<T, Object> {


    MockInterface(Class<T> mockClass) {
        super(mockClass);
        InvocationHandler invocationHandler = (proxy, method, args) -> {
            currentMethodValue = methodValueMap.computeIfAbsent(method, MethodValue::new);
            currentMethodValue.args(args);
            return PrimitiveEnum.get(method.getReturnType()).zero_value;
        };
        instance = (T) Proxy.newProxyInstance(mockClass.getClassLoader(), new Class[]{mockClass}, invocationHandler);
    }


    public T build() {
        return (T) Proxy.newProxyInstance(mockClass.getClassLoader(), new Class[]{mockClass}, (proxy, method, args) -> getMethodValueOfInterface(method, args));
    }


}
