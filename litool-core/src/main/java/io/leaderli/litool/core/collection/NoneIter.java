package io.leaderli.litool.core.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author leaderli
 * @since 2022/7/18
 */
public class NoneIter<T> implements Iterator<T> {

    private static final NoneIter<?> NONE = new NoneIter<>();

    private NoneIter() {

    }

    @SuppressWarnings("unchecked")
    public static <T> NoneIter<T> of() {
        return (NoneIter<T>) NONE;
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
