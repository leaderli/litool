package io.leaderli.litool.core.lang.lean;

import java.lang.reflect.Type;

/**
 * @author leaderli
 * @since 2022/9/29
 */
public interface NullableTypeAdapters<T> extends TypeAdapter<T> {
    T read(Lean lean, Object source, Type targetType);
}
