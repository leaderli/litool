package io.leaderli.litool.test;

import io.leaderli.litool.core.type.PrimitiveEnum;
import io.leaderli.litool.core.type.TypeUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class MethodValueRecorder {

    protected final Class<?> mockClass;
    protected final Map<Method, MethodValue> methodValueMap = new HashMap<>();

    protected MethodValueRecorder(Class<?> mockClass) {
        this.mockClass = mockClass;
    }


    protected Object getMethodValueOfInterface(Type type, Method m, Object[] args, BiFunction<Method, Object[], Object> otherInvocationHandler) {


        Object returnValue = null;
        if (methodValueMap.containsKey(m)) {
            returnValue = getMethodValue(m, args);
        } else {
            if (otherInvocationHandler != null) {
                returnValue = otherInvocationHandler.apply(m, args);
            }
        }
        returnValue = MethodValueFactory.adjustReturnValue(returnValue, type, m.getGenericReturnType()).get();
        // 模拟接口没有兜底返回值，必须修正基础类型返回为null的情况
        Type returnType = TypeUtil.resolve(type, m.getGenericReturnType());
        return PrimitiveEnum.get(returnType).read(returnValue);
    }

    protected Object getMethodValue(Method m, Object[] args) {
        MethodValue<Object> methodValue = methodValueMap.get(m);
        return methodValue.getMethodValue(args);
    }


}
