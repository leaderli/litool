package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.Either;
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

    public MethodValueRecorder(Class<?> mockClass) {
        this.mockClass = mockClass;
    }

    protected Object getMethodValueOfInterface(Type type, Method m, Object[] args, BiFunction<Method, Object[], Object> otherInvocationHandler) {


        Object apply = null;
        if (methodValueMap.containsKey(m)) {

            apply = getMethodValue(m, args);
        } else {
            if (otherInvocationHandler != null) {
                apply = otherInvocationHandler.apply(m, args);
            }
        }
        if (apply instanceof Either) {
            apply = ((Either<?, ?>) apply).get();
        }
        if (apply == null) {
            return PrimitiveEnum.get(TypeUtil.resolve(type, m.getGenericReturnType())).zero_value;
        }
        return apply;
    }

    protected Object getMethodValue(Method m, Object[] args) {
        MethodValue<Object> methodValue = methodValueMap.get(m);
        return methodValue.getMethodValue(args);
    }
}
