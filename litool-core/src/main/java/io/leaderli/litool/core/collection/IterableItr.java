package io.leaderli.litool.core.collection;

import java.util.Iterator;

/**
 * Provides a synthetic interface with both {@link Iterable} and {@link Iterator}
 *
 * @param <T> the type of elements returned by the iterator
 */
public interface IterableItr<T> extends Iterable<T>, Iterator<T> {


    @Override
    default Iterator<T> iterator() {
        return this;
    }
}
