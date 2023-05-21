package io.leaderli.litool.core.test;

import java.lang.reflect.AnnotatedElement;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class ObjectCustomCartesian implements CustomValuable<ObjectValues> {

    @Override
    public Object[] cartesian(Class<?> type, ObjectValues annotation, AnnotatedElement annotatedElement, CartesianContext context) {
        return new CartesianObject<>(type, field -> CartesianUtil.cartesian(field, context))
                .cartesian()
                .toArray(Object.class);
    }
}
