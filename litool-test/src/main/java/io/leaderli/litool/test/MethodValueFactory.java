package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.Either;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.MethodFilter;
import io.leaderli.litool.core.type.PrimitiveEnum;
import io.leaderli.litool.core.type.TypeUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodValueFactory {

    /**
     * 每个class对应一套挡板map，每个挡板根据每个方法设定一个挡板list，按照顺序依次拦截
     */
    private static final Map<String, ClassMock> mockClasses = new HashMap<>();
    private static final Map<String, LiTuple<MethodProxy<?>, Method>> invokers = new HashMap<>();
    public static final Map<String, LiTuple<MethodAssert, Method>> recorders = new HashMap<>();


    public static ClassMock getClassMockers(Class<?> clazz) {
        return mockClasses.computeIfAbsent(clazz.getName(), k -> new ClassMock(clazz));
    }

//    public static List<LiTuple<Method, MethodProxy<?>>> getMethodMockers(Class<?> clazz, String method) {
//        return getClassMockers(clazz).computeIfAbsent(method, k -> new ArrayList<>());
//    }

    public static void putMethodProxy(Class<?> clazz, Method method, MethodProxy<?> methodProxy, MethodFilter methodFilter) {
        if (Boolean.TRUE.equals(methodFilter.apply(method))) {
            getClassMockers(clazz);
            getMethodMockers(clazz, method + "").add(0, LiTuple.of(method, methodProxy));
        }
    }

    public static Either<?, ?> invoke(Class<?> clazz, String method, Object bean, Object[] args) throws Throwable {

        List<LiTuple<Method, MethodProxy<?>>> methodMockers = getMethodMockers(clazz, method);
        for (LiTuple<Method, MethodProxy<?>> methodProxyTuple : methodMockers) {
            Either<Void, Object> returnValue = adjustReturnValue(methodProxyTuple._2.apply(bean, methodProxyTuple._1, args), clazz, methodProxyTuple._1.getGenericReturnType());
            if (returnValue.isRight()) {
                return returnValue;
            }
        }
        return Either.none();
    }

    /**
     * @param returnValue       返回值
     * @param clazz             方法的声明类
     * @param genericReturnType 方法
     * @return 根据返回类型修正返回值，如果为null，则返回零值，如果类型不匹配，则返回 {@link  Either#none()}。
     * @see PrimitiveEnum#zero_value
     */
    public static Either<Void, Object> adjustReturnValue(Object returnValue, Type clazz, Type genericReturnType) {

        // 不mock
        if (returnValue == LiMock.SKIP_MARK) {
            return Either.none();
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
            AbstractRecorder.assertThrow.add(throwable);
            throw throwable;
        }
    }
}
