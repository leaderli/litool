package io.leaderli.litool.core.test;

import io.leaderli.litool.core.type.MetaFunction;

import java.lang.annotation.Annotation;

/**
 * a interface provide potential values of field or parameter
 *
 * @author leaderli
 * @since 2022/8/20
 */
@FunctionalInterface
public interface CartesianFunction<A extends Annotation, R> extends MetaFunction<A, CartesianContext, R[]> {


    @Override
    R[] apply(A annotatedByValuable, CartesianContext context);
}
