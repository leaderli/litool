package io.leaderli.litool.core.lang.lean;

import java.lang.reflect.Field;

/**
 * @author leaderli
 * @since 2022/9/26 11:53 AM
 */
public class FieldAnnotationName implements LeanField<LeanName> {


    @Override
    public String apply(LeanName annotatedByValuable, Field field) {
        return annotatedByValuable.value();
    }
}
