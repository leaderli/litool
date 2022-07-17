package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.Lino;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @author leaderli
 * @since 2022/6/26 7:00 AM
 */
public class LiMethodUtil {

    /**
     * @param method 源方法
     * @param obj    查找的类
     * @return 返回 obj 中 同名、参数类型相同，返回类型的方法，且必须是 public 方法
     */
    public static Lino<Method> getSameSignatureMethod(Method method, Object obj) {

        return getSameSignatureMethod(method, Lino.of(obj).map(Object::getClass));
    }

    private static Lino<Method> getSameSignatureMethod(Method origin, Lino<Class<?>> type) {

        return type.map(Class::getMethods)
                .filter(Modifier.isPublic(origin.getModifiers()))
                .toLira(Method.class)
                .filter(m -> origin.getName().equals(m.getName()))
                .filter(m -> Arrays.equals(m.getParameterTypes(), origin.getParameterTypes()))
                .first();
    }

    /**
     * @param method 源方法
     * @param type   查找的类型
     * @return 返回 type 中 同名、参数类型相同，返回类型的方法，且必须是 public 方法
     */
    public static Lino<Method> getSameSignatureMethod(Method method, Class<?> type) {

        return getSameSignatureMethod(method, Lino.of(type));
    }
}
