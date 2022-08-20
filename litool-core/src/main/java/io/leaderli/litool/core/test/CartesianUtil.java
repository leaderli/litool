package io.leaderli.litool.core.test;

import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.PrimitiveEnum;
import io.leaderli.litool.core.type.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/19 8:10 AM
 */
public class CartesianUtil {


    private static final Map<Valuable, CartesianFunction<Annotation, Object>> CARTESIAN_FUNCTION_MAP = new HashMap<>();

    public static <T> Object[] cartesian(Field field) {

        Class<?> type = field.getType();


        return ReflectUtil.findAnnotationsWithMark(field, Valuable.class)
                .first()
                .map(CartesianUtil::provideByValuable)
                .or(cartesian(type))
                .get();


    }

    @SuppressWarnings("unchecked")
    public static <T> T[] cartesian(Class<T> cls) {

        PrimitiveEnum primitive = PrimitiveEnum.get(cls);
        Object def = primitive.def;
        Object arr = Array.newInstance(ClassUtil.primitiveToWrapper(cls), 1);
        Array.set(arr, 0, def);
        return (T[]) arr;
    }

    /**
     * @param mark 被 {@link Valuable} 注解的注解
     * @return 使用  {@link Valuable#value()} 的 apply 函数，返回一个数组
     * @see CartesianFunction#apply(Annotation)
     */
    @SuppressWarnings("unchecked")
    public static Object[] provideByValuable(Annotation mark) {


        Valuable valuable = mark.annotationType().getAnnotation(Valuable.class);
        return CARTESIAN_FUNCTION_MAP
                .computeIfAbsent(valuable, an -> {

                    Class<? extends CartesianFunction<?, ?>> value = an.value();
                    return (CartesianFunction<Annotation, Object>) RuntimeExceptionTransfer.get(value::newInstance);
                })
                .apply(mark);
    }
}
