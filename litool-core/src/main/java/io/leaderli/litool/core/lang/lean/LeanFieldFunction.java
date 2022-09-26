package io.leaderli.litool.core.lang.lean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author leaderli
 * @since 2022/9/26 12:02 PM
 */
public interface LeanFieldFunction<A extends Annotation> {
    String apply(A annotation, Field field);
}
