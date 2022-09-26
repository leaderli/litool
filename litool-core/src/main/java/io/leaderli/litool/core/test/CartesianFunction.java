package io.leaderli.litool.core.test;

import java.lang.annotation.Annotation;

/**
 * a interface provide potential values of field or parameter
 *
 * @author leaderli
 * @since 2022/8/20
 */
@FunctionalInterface
public interface CartesianFunction<T extends Annotation, R> {


    R[] apply(T t, CartesianContext context);
}
