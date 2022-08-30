package io.leaderli.litool.core.collection;

import java.util.Iterator;

/**
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
}
