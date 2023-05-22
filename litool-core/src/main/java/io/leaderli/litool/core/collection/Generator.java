package io.leaderli.litool.core.collection;

import java.util.Iterator;

/**
 * the interface that indicate this is an iterator that never stops, such as {@link  IntGenerator}
 *
 * @author leaderli
 * @since 2022/8/30
 */
public interface Generator<T> extends IterableItr<T> {
    @Override
    default boolean hasNext() {
        return true;
    }

    @Override
    default Iterator<T> iterator() {
        return this;
    }

    @Override
    default String name() {
        return "Generator";
    }
}
