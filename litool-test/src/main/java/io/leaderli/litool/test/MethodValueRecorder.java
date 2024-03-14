package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.Either;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.PrimitiveEnum;
import io.leaderli.litool.core.type.TypeUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class MethodValueRecorder {

    public static final Map<String, LiTuple<MethodProxy<?>, Method>> invokers = new HashMap<>();
    public static final Map<String, LiTuple<MethodAssert, Method>> recorders = new HashMap<>();
    protected final Class<?> mockClass;
    protected final Map<Method, MethodValue> methodValueMap = new HashMap<>();

    public MethodValueRecorder(Class<?> mockClass) {
        this.mockClass = mockClass;
    }

    public static Either<?, ?> invoke(String uuid, Class<?> clazz, String name, Class<?>[] argsType, Object[] args) throws Throwable {

        LiTuple<MethodProxy<?>, Method> tuple = invokers.get(uuid);
        if (tuple == null) {
            return Either.none();
        }
        Object returnValue = tuple._1.apply(tuple._2, args);
        return adjustReturnValue(returnValue, clazz, tuple._2.getGenericReturnType());

    }

    /**
     * @param returnValue       返回值
     * @param clazz             方法的声明类
     * @param genericReturnType 方法
     * @return 根据返回类型修正返回值，如果为null，则返回零值，如果类型不匹配，则返回 {@link  Either#none()}。
     * @see PrimitiveEnum#zero_value
     */
    public static Either<Void, Object> adjustReturnValue(Object returnValue, Type clazz, Type genericReturnType) {
        if (returnValue instanceof Either) {
            if (((Either<?, ?>) returnValue).isLeft()) {
                return Either.none();
            }
            returnValue = ((Either<?, ?>) returnValue).getRight();
        }

        // 尝试将泛型返回类型解析为实际类型
        Class<?> returnType = TypeUtil.erase(TypeUtil.resolve(clazz, genericReturnType));
        if (returnValue == null) {
            return Either.right(PrimitiveEnum.get(returnType).zero_value);
        }
        if (ClassUtil.isInstanceof(returnValue, returnType)) {
            return Either.right(returnValue);
        }
        return Either.none();
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
        returnValue = adjustReturnValue(returnValue, type, m.getGenericReturnType()).get();
        // 模拟接口没有兜底返回值，必须修正基础类型返回为null的情况
        return PrimitiveEnum.get(m.getReturnType()).read(returnValue);
    }

    protected Object getMethodValue(Method m, Object[] args) {
        MethodValue<Object> methodValue = methodValueMap.get(m);
        return methodValue.getMethodValue(args);
    }


    /**
     * 用于方法调用的断言
     */
    public static void record(String uuid, Object _this, Object[] args, Object _return) {
        LiTuple<MethodAssert, Method> tuple = recorders.get(uuid);
        if (tuple == null) {
            return;
        }
        try {
            tuple._1.apply(tuple._2, args, _return);
        } catch (Throwable throwable) {
            Recorder.assertThrow.add(throwable);
            throw throwable;
        }
    }
}
