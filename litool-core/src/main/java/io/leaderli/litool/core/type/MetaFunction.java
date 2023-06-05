package io.leaderli.litool.core.type;

import java.lang.annotation.Annotation;


/**
 * 元注解的处理函数接口，注解里有一个方法，该方法返回值为该接口的实现类
 *
 * @param <A> 注解类型，该注解被元注解修饰
 * @param <T> apply方法的第二个参数类型
 * @param <R> apply方法的返回值类型
 * @since 2022/9/26
 */
@FunctionalInterface
public interface MetaFunction<A extends Annotation, T, R> {

    R apply(A annotatedByValuable, T t);
}
