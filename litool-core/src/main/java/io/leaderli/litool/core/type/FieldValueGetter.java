package io.leaderli.litool.core.type;

import java.lang.reflect.Field;

/**
 *
 */
@FunctionalInterface
public interface FieldValueGetter<T> {

    Object getValue(T bean, Field field, Class<?> fieldType);
}
