package io.leaderli.litool.core.collection;

import java.util.NoSuchElementException;

/**
 * @author leaderli
 * @since 2022/7/18
 */
public class NoneItr<T> implements IterableItr<T> {

    private static final NoneItr<?> NONE = new NoneItr<>();

    private NoneItr() {

    }

    @SuppressWarnings("unchecked")
    public static <T> NoneItr<T> of() {
        return (NoneItr<T>) NONE;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        throw new NoSuchElementException();
    }
}
