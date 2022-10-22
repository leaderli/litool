package io.leaderli.litool.core.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public interface CustomValuable<A extends Annotation> {
    Object[] cartesian(Class<?> type, A annotation, AnnotatedElement annotatedElement, CartesianContext context);
}
