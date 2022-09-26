package io.leaderli.litool.core.type;

import java.lang.annotation.Annotation;

/**
 * A interface use for meta annotation, which annotation have a method like
 * {@code      Class<? extends MetaFunction<A,T R>> value();}
 *
 * @param <A> the
 * @param <T>
 * @param <R>
 * @author leaderli
 * @since 2022/9/26
 */
@FunctionalInterface
public interface MetaFunction<A extends Annotation, T, R> {

    R[] apply(A ma, T t);
}
