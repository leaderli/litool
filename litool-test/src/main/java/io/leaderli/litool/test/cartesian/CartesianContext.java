package io.leaderli.litool.test.cartesian;

import io.leaderli.litool.core.meta.Either;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.MetaAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;

/**
 * a context of cartesian
 *
 * @author leaderli
 * @since 2022/8/21
 */
public class CartesianContext {

    private final MetaAnnotation<Valuable, CartesianFunction<Annotation, Object>> valuableMeta = new MetaAnnotation<>(Valuable.class,
            new LiTypeToken<CartesianFunction<Annotation, Object>>() {
            }.getGenericType());

    @SuppressWarnings("rawtypes")
    private final Map<Class<?>, CustomValuable> customValuables = new HashMap<>();

    public CartesianContext() {
        registerCustomValuable(ObjectValues.class, new ObjectCustomCartesian());
        registerCustomValuable(DynamicValues.class, new DynamicCustomCartesian());
    }

    Lira<LiTuple<CartesianFunction<Annotation, Object>, Annotation>> relatives(AnnotatedElement annotatedElement) {
        return valuableMeta.relatives(annotatedElement);
    }

    Lino<LiTuple<CartesianFunction<Annotation, Object>, Annotation>> relative(AnnotatedElement annotatedElement) {
        return valuableMeta.relative(annotatedElement);
    }

    public <A extends Annotation> void registerCustomValuable(Class<A> annotationClass, CustomValuable<A> customValuable) {
        customValuables.put(annotationClass, customValuable);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Either<Integer, Object[]> custom(Class<?> type, AnnotatedElement annotatedElement) {
        Lira<Annotation> classLira = Lira.of(annotatedElement.getAnnotations());

        Lino<LiTuple<Annotation, CustomValuable>> first = classLira
                .tuple(an -> customValuables.get(an.annotationType()))
                .filter(LiTuple::isRight)
                .first();

        if (first.absent()) {
            return Either.left(1);
        }
        return first
                .map(tuple2 -> tuple2._2.cartesian(type, tuple2._1, annotatedElement, this))
                .map(Either::<Integer, Object[]>right).get();


    }

}
