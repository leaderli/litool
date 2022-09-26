package io.leaderli.litool.core.test;

import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.MetaAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * a context of cartesian
 *
 * @author leaderli
 * @since 2022/8/21
 */
@SuppressWarnings({"rawtypes"})
public class CartesianContext {

    private final MetaAnnotation<Valuable, CartesianFunction> valuableMeta = new MetaAnnotation<>(Valuable.class, CartesianFunction.class);

    Lira<LiTuple2<CartesianFunction, Annotation>> relatives(AnnotatedElement annotatedElement) {
        return valuableMeta.relatives(annotatedElement);
    }


}
