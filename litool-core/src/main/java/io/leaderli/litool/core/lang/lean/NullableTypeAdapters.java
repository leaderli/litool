package io.leaderli.litool.core.lang.lean;

import java.lang.reflect.Type;

/**
 * 定义了当 Source 为 null 时，如何进行转换的类型适配器
 *
 * @param <T> 要适配的类型
 */
public interface NullableTypeAdapters<T> extends TypeAdapter<T> {
    /**
     * 从null中读取类型为targetType的数据，并将其转换为T类型。
     *
     * @param lean        Lean对象，用于读取数据
     * @param ownerSource 为null的数据源所属的源
     * @param targetType  目标类型
     * @return 转换后的T类型数据
     */
    T read(Lean lean, Object ownerSource, Type targetType);
}
