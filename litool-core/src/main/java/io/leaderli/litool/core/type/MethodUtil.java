package io.leaderli.litool.core.type;

import io.leaderli.litool.core.exception.LiAssertUtil;

import java.lang.annotation.Repeatable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 提供了一系列工具方法，用于操作Method对象。
 *
 * @author leaderli
 * @since 2022/6/26 7:00 AM
 */
public class MethodUtil {
    public static final String CLINIT_METHOD_NAME = "<clinit>";

    /**
     * 获取与参考方法具有相同签名的方法对象。
     *
     * @param lookup          要查找方法的类
     * @param referenceMethod 参考方法的方法
     * @return 与参考方法具有相同签名的方法对象或null（如果找不到匹配的方法）
     */
    public static Method getSameSignatureMethod(LiTypeToken<?> lookup, Method referenceMethod) {
        return getSameSignatureMethod(lookup, MethodSignature.non_strict(referenceMethod));
    }

    /**
     * 获取与参考方法具有相同签名的方法对象。
     *
     * @param lookup                   要查找方法的类
     * @param referenceMethodSignature 参考方法的方法签名
     * @return 与参考方法具有相同签名的方法对象或null（如果找不到匹配的方法）
     */
    public static Method getSameSignatureMethod(LiTypeToken<?> lookup, MethodSignature referenceMethodSignature) {
        for (Method m : ReflectUtil.getMethods(lookup.getRawType())) {

            if (m.isBridge()) {
                continue;
            }
            Type returnType = TypeUtil.resolve(lookup, m.getGenericReturnType());
            Type[] parameterTypes = Stream.of(m.getGenericParameterTypes())
                    .map(t -> TypeUtil.resolve(lookup, t))
                    .toArray(Type[]::new);
            MethodSignature x = new MethodSignature(m.getName(), returnType, parameterTypes);
            if (referenceMethodSignature.equals(x)) {
                return m;
            }
        }
        return null;
    }

    /**
     * 判断方法是否不属于Object类。
     *
     * @param method 要判断的方法
     * @return 如果方法不属于Object类，则返回true，否则返回false。
     * @see #belongsTo(Method, Class)
     */
    public static boolean notObjectMethod(Method method) {
        return !belongsTo(method, Object.class);
    }

    /**
     * 判断方法是否属于指定的类。
     *
     * @param method 要判断的方法
     * @param cls    要比较的类
     * @return 如果方法属于指定的类，则返回true，否则返回false。
     */
    public static boolean belongsTo(Method method, Class<?> cls) {
        return method.getDeclaringClass() == cls;
    }


    /**
     * 判断方法是否是 {@link  Repeatable#value()} 方法的实现。
     *
     * @param method 要判断的方法
     * @return 如果方法是 {@link  Repeatable#value()} 方法的实现，则返回true，否则返回false。
     */
    public static boolean methodOfRepeatableContainer(Method method) {

        if (method == null) {
            return false;
        }
        return method.getName().equals("value")
                && method.getDeclaringClass().isAnnotation()
                && method.getParameterTypes().length == 0
                && method.getReturnType().isArray()
                && method.getReturnType().getComponentType().isAnnotation()
                && method.getReturnType().getComponentType().isAnnotationPresent(Repeatable.class);
    }


    /**
     * eg:
     * <p>
     * {@link Object#toString()}
     * <pre>
     *  Object#toString():String
     * </pre>
     *
     * @param method 方法
     * @return 方法的简短字符串表现形式，包含参数类型和返回类型
     */
    public static String shortString(Method method) {

        String parameters = Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(",", "(", ")"));
        return veryShortString(method) + parameters + ":" + method.getReturnType().getSimpleName();
    }

    /**
     * eg:
     * <p>
     * {@link Object#toString()}
     * <pre>
     *  Object#toString
     * </pre>
     *
     * @param method 方法
     * @return 方法的简短字符串表现形式，不包含参数类型和返回类型
     */
    public static String veryShortString(Method method) {
        return method.getDeclaringClass().getSimpleName() + "#" + method.getName();
    }

    public static void onlyCallByCLINIT() {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
        LiAssertUtil.assertTrue(MethodUtil.CLINIT_METHOD_NAME.equals(caller.getMethodName()), "only support call in <clinit>");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object methodInvoke(Class clazz, String name, Class[] parameterType, Object[] parameter, MethodInvoker methodInvoker) {
        try {
            Method method = clazz.getDeclaredMethod(name, parameterType);
            return methodInvoker.invoke(method, parameter);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }
}
