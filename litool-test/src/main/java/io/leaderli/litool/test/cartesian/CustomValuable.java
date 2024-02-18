package io.leaderli.litool.test.cartesian;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public interface CustomValuable<A extends Annotation> {
    Object[] cartesian(Class<?> type, A annotation, AnnotatedElement annotatedElement, CartesianContext context);
}
