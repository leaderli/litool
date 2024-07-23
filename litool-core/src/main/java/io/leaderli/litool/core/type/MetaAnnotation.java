package io.leaderli.litool.core.type;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.internal.ParameterizedTypeImpl;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 用于获取特定注解下另一个注解的信息，同时提供了创建一个对象的方法
 *
 * @param <A> 特定注解
 * @param <F> 另一个注解
 */
public class MetaAnnotation<A extends Annotation, F extends MetaFunction<? extends Annotation, ?, ?>> {

    public final Map<A, F> meta_function = new HashMap<>();
    public final Class<A> metaClass;
    public final Class<F> functionClass;

    @SuppressWarnings("unchecked")
    public MetaAnnotation(Class<A> metaClass, LiTypeToken<F> typeToken) {
        this(metaClass, (Class<F>) typeToken.getRawType());
    }

    public MetaAnnotation(Class<A> metaClass, Class<F> functionClass) {

        Objects.requireNonNull(metaClass);
        LiAssertUtil.assertTrue(metaClass.isAnnotation());

        Method method = ReflectUtil.getMethod(metaClass, "value").get();
        Objects.requireNonNull(method);


        Class<?> metaFunctionType =
                ParameterizedTypeImpl.make(method.getGenericReturnType()).getActualClassArgument().get();

        LiAssertUtil.assertTrue(metaFunctionType == functionClass);

        this.metaClass = metaClass;
        this.functionClass = functionClass;
    }

    /**
     * 获取特定注解下另一个注解的信息
     *
     * @param annotatedElement 被特定注解标记的元素
     * @return 包含另一个注解和特定注解的元组
     */
    public Lino<LiTuple<F, Annotation>> relative(AnnotatedElement annotatedElement) {
        return relatives(annotatedElement).first();
    }

    /**
     * 获取特定注解下另一个注解的信息
     *
     * @param annotatedElement 被特定注解标记的元素
     * @return 包含另一个注解和特定注解的元组的列表
     */
    public Lira<LiTuple<F, Annotation>> relatives(AnnotatedElement annotatedElement) {
        return ReflectUtil.findAnnotationsWithMetaAnnotation(annotatedElement, metaClass)
                .tuple(an -> an.annotationType().getAnnotation(metaClass))
                .map(tu -> tu.map2(metaInstance -> this.computeIfAbsent(tu._1, tu._2))
                        .swap()         // swap to human readable
                )
                .assertTrue(a ->
                        {
                            // if the annotated-annotation with meta, and meta's metaFunction first generic-type is
                            // not same the annotated-annotation
                            return TypeUtil.resolve2Parameterized(a._1.getClass(), MetaFunction.class).getActualClassArgument()
                                    .filter(annotatedByMeta -> annotatedByMeta == a._2.annotationType());

                        }, a -> {

                            String expect = TypeUtil.resolve2Parameterized(a._1.getClass(), MetaFunction.class).getActualClassArgument().get() + "";
                            return "\r\nexpect:" + expect + "\r\nactual:" + a._2.annotationType();
                        }
                );

    }

    /**
     * 计算并返回另一个注解的对象
     *
     * @param annotated    注释
     * @param metaInstance 特定注解的实例
     * @return 另一个注解的对象
     */
    private F computeIfAbsent(Annotation annotated, A metaInstance) {
        return meta_function.computeIfAbsent(metaInstance, k -> createBy(annotated, metaInstance));
    }

    /**
     * 创建另一个注解的对象
     *
     * @param annotated    注释
     * @param metaInstance 特定注解的实例
     * @return 另一个注解的对象
     */
    @SuppressWarnings("unchecked")
    private F createBy(Annotation annotated, A metaInstance) {
        return (F) ReflectUtil.invokeMethodByName(metaInstance, "value")
                .cast(Class.class)
                .unzip(ReflectUtil::newInstance)
                .cast(functionClass)
                .get();
    }
}
