package io.leaderli.litool.core.type;

import io.leaderli.litool.core.exception.UnsupportedTypeException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public class TypeUtil {

/**
 * 是否未知类型<br>
 * type为null或者{@link TypeVariable} 都视为未知类型
 *
 * @param type Type类型
 * @return 是否未知类型
 */
public static boolean isUnknown(Type type) {
    return null == type || type instanceof TypeVariable;
}

public static Class<?> getClass(Type type) {
    if (type == null) {
        return null;
    }
    if (type instanceof Class) {
        return (Class<?>) type;
    }
    if (type instanceof ParameterizedType) {
        return (Class<?>) ((ParameterizedType) type).getRawType();
    }

    throw new UnsupportedTypeException(type);
}

/**
 * Return two TypeVariable typeName is equals
 *
 * @param left  a TypeVariable
 * @param right a TypeVariable
 * @return two TypeVariable typeName is equals
 */
public static boolean equals(TypeVariable<?> left, TypeVariable<?> right) {
    return Objects.equals(left.getTypeName(), right.getTypeName());
}

public static boolean equals(ParameterizedType left, ParameterizedType right) {

    if (left.getRawType() == right.getRawType()) {

        Type[] leftActualTypeArguments = left.getActualTypeArguments();
        Type[] rightActualTypeArguments = right.getActualTypeArguments();
        if (leftActualTypeArguments.length != rightActualTypeArguments.length) {
            return false;
        }

        for (int i = 0; i < leftActualTypeArguments.length; i++) {
            if (!equals(leftActualTypeArguments[i], rightActualTypeArguments[i])) {
                return false;
            }
        }

        return true;
    }
    return false;
}

/**
 * @param left  type
 * @param right type
 * @return 判断两个 type 是否相等
 */
public static boolean equals(Type left, Type right) {
    if (left instanceof ParameterizedType && right instanceof ParameterizedType) {
        return equals((ParameterizedType) left, (ParameterizedType) right);
    }
    if (left instanceof TypeVariable && right instanceof TypeVariable) {
        return equals((TypeVariable<?>) left, (TypeVariable<?>) right);
    }
    return Objects.equals(left, right);
}

}
