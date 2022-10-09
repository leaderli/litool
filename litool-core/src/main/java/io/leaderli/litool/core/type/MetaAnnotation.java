package io.leaderli.litool.core.type;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.internal.ParameterizedTypeImpl;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/9/26 12:34 PM
 */
public class MetaAnnotation<A extends Annotation, F extends MetaFunction<? extends Annotation, ?, ?>> {

    public final Map<A, F> meta_function = new HashMap<>();
    public final Class<A> meta;
    public final Class<F> functionType;

    @SuppressWarnings("unchecked")
    public MetaAnnotation(Class<A> meta, LiTypeToken<F> typeToken) {
        this(meta, (Class<F>) typeToken.getRawType());
    }

    public MetaAnnotation(Class<A> meta, Class<F> functionType) {

        Objects.requireNonNull(meta);
        LiAssertUtil.assertTrue(meta.isAnnotation());

        Method method = ReflectUtil.getMethod(meta, "value").get();
        Objects.requireNonNull(method);


        Class<?> metaFunctionType =
                ParameterizedTypeImpl.make(method.getGenericReturnType()).getActualClassArgument().get();

        LiAssertUtil.assertTrue(metaFunctionType == functionType);

        this.meta = meta;
        this.functionType = functionType;
    }


    public Lino<LiTuple2<F, Annotation>> relative(AnnotatedElement annotatedElement) {
        return relatives(annotatedElement).first();
    }

    public Lira<LiTuple2<F, Annotation>> relatives(AnnotatedElement annotatedElement) {
        return ReflectUtil.findAnnotationsWithMetaAnnotation(annotatedElement, meta)
                .tuple(an -> an.annotationType().getAnnotation(meta))
                .map(tu -> tu.map2(metaInstance -> this.compute(tu._1, tu._2))
                        .swap()         // swap to human readable
                )
                .assertTrue(a ->
                        // if the annotated-annotation with meta, and meta's metaFunction first generic-type is
                        // not same the annotated-annotation
                        TypeUtil.resolve2Parameterized(a._1.getClass(), MetaFunction.class).getActualClassArgument()
                                .filter(annotatedByMeta -> annotatedByMeta == a._2.annotationType())
                );

    }

    private F compute(Annotation annotated, A metaInstance) {
        return meta_function.computeIfAbsent(metaInstance, k -> createBy(annotated, metaInstance));
    }

    @SuppressWarnings("unchecked")
    private F createBy(Annotation annotated, A metaInstance) {
        return (F) ReflectUtil.invokeMethodByName(metaInstance, "value")
                .cast(Class.class)
                .unzip(ReflectUtil::newInstance)
                .cast(functionType)
                .get();
    }
}
