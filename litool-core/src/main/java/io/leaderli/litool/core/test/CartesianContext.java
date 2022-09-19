package io.leaderli.litool.core.test;

import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * a context of cartesian
 *
 * @author leaderli
 * @since 2022/8/21
 */
public class CartesianContext {
    private final Map<Valuable, CartesianFunction<Annotation, Object>> valuableCartesianFunctionMap = new HashMap<>();


    /**
     * Return the potential values of annotation provide
     *
     * @param valuableAnnotation the annotation that annotated by {@link Valuable}
     * @return the value called the {@link  CartesianFunction#apply(Annotation, CartesianContext)} by the  instance
     * of {@link Valuable#value()}
     * @see CartesianFunction#apply(Annotation, CartesianContext)
     */
    @SuppressWarnings("unchecked")
    public Object[] provideByValuable(Annotation valuableAnnotation) {


        Valuable valuable = valuableAnnotation.annotationType().getAnnotation(Valuable.class);
        return valuableCartesianFunctionMap
                .computeIfAbsent(valuable, an -> {

                    Class<? extends CartesianFunction<?, ?>> value = an.value();
                    return (CartesianFunction<Annotation, Object>) RuntimeExceptionTransfer.get(() -> value.getDeclaredConstructor().newInstance());
                })
                .apply(valuableAnnotation, this);
    }


}
