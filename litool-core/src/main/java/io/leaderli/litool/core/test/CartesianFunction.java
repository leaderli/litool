package io.leaderli.litool.core.test;

import java.lang.annotation.Annotation;

/**
 * @author leaderli
 * @since 2022/8/20
 */
public interface CartesianFunction<T extends Annotation, R> {
R[] apply(T t, CartesianContext context);
}
