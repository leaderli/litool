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

    /**
     * Returns an {@link  ArrayItr} over elements of type {@code T}.
     * null or empty array will always return {@link  NoneItr#of()}
     *
     * @param elements an elements
     * @param <T>      the type of elements
     * @return Returns an iterator over elements of type {@code T}.
     */
    @SafeVarargs
    static <T> IterableItr<T> of(T... elements) {
        if (elements == null || elements.length == 0) {
            return NoneItr.of();
        }
        return new ArrayItr<>(elements);
    }
}
