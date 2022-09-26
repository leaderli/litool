package io.leaderli.litool.core.lang.lean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author leaderli
 * @since 2022/9/26 12:02 PM
 */
@FunctionalInterface
public interface LeanField<A extends Annotation> {
    String apply(A annotated, Field field);
}
