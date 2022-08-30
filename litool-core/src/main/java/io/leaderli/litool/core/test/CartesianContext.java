package io.leaderli.litool.core.test;

import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class CartesianContext {
    private final Map<Valuable, CartesianFunction<Annotation, Object>> valuableCartesianFunctionMap = new HashMap<>();


    /**
     * @param mark 被 {@link Valuable} 注解的注解
     * @return 使用  {@link Valuable#value()} 的 apply 函数，返回一个数组
     * @see CartesianFunction#apply(Annotation, CartesianContext)
     */
    @SuppressWarnings("unchecked")
    public Object[] provideByValuable(Annotation mark) {


        Valuable valuable = mark.annotationType().getAnnotation(Valuable.class);
        return valuableCartesianFunctionMap
                .computeIfAbsent(valuable, an -> {

                    Class<? extends CartesianFunction<?, ?>> value = an.value();
                    return (CartesianFunction<Annotation, Object>) RuntimeExceptionTransfer.get(value::newInstance);
                })
                .apply(mark, this);
    }


}
