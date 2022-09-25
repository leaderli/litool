package io.leaderli.litool.core.type;

import java.lang.reflect.Type;

/**
 * @param <T> the type of object that will be created by this implementation.
 * @author leaderli
 * @since 2022/9/25
 */
@FunctionalInterface
public interface InstanceCreator<T> {
    T createInstance(Type type);
}
