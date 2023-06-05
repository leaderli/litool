package io.leaderli.litool.core.type;

import java.lang.reflect.Type;

/**
 * 用于创建指定类型的对象。
 *
 * @param <T> 将由此实现创建的对象的类型。
 * @since 2022/9/25
 */
@FunctionalInterface
public interface InstanceCreator<T> {

    /**
     * 创建指定类型的对象。
     *
     * @param type 要创建的对象的类型。
     * @return 创建的对象。
     */
    T createInstance(Type type);
}
