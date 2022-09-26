package io.leaderli.litool.core.lang.lean;

import java.lang.reflect.Field;

/**
 * @author leaderli
 * @since 2022/9/26 11:53 AM
 */
public class FieldAnnotationName implements LeanFieldFunction<LeanFieldName> {


    @Override
    public String apply(LeanFieldName annotation, Field field) {
        return annotation.value();
    }
}
