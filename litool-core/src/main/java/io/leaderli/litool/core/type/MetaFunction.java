package io.leaderli.litool.core.type;

import java.lang.annotation.Annotation;

/**
 * A interface use for meta annotation, which annotation have a method like
 * {@code      Class<? extends MetaFunction<A,T R>> value();}
 *
 * @param <A> the type of annotation which is annotated by meta annotation
 * @param <T> the type of 2nd parameter of the {@link #apply(Annotation, Object)}
 * @param <R> the function result type
 * @author leaderli
 * @since 2022/9/26
 */
@FunctionalInterface
public interface MetaFunction<A extends Annotation, T, R> {

    R apply(A annotatedByValuable, T t);
}
