package io.leaderli.litool.core.test;

import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.MetaAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * a context of cartesian
 *
 * @author leaderli
 * @since 2022/8/21
 */
public class CartesianContext {

    private final MetaAnnotation<Valuable, CartesianFunction<Annotation, Object>> valuableMeta =
            new MetaAnnotation<>(Valuable.class, LiTypeToken.of(CartesianFunction.class));

    Lira<LiTuple2<CartesianFunction<Annotation, Object>, Annotation>> relatives(AnnotatedElement annotatedElement) {
        return valuableMeta.relatives(annotatedElement);
    }

    Lino<LiTuple2<CartesianFunction<Annotation, Object>, Annotation>> relative(AnnotatedElement annotatedElement) {
        return valuableMeta.relative(annotatedElement);
    }

}
