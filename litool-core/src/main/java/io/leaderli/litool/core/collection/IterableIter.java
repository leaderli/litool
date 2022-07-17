package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.meta.Lino;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author leaderli
 * @since 2022/7/17
 */
public class IterableIter<T> implements Iterator<T> {


    private final Iterator<T> iterator;

    public IterableIter(Iterable<T> iterable) {
        this(Lino.of(iterable).map(Iterable::iterator).get());
    }

    public IterableIter(Iterator<T> iterator) {

        this.iterator = iterator;
    }

    public static <T> IterableIter<T> of(Iterator<T> iterator) {
        return new IterableIter<>(iterator);
    }

    public static <T> IterableIter<T> of(Iterable<T> iterable) {
        return new IterableIter<>(iterable);
    }

    @Override
    public boolean hasNext() {
        return iterator != null && iterator.hasNext();
    }

    @Override
    public T next() {

        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return iterator.next();
    }

    @Override
    public void remove() {
        if (hasNext()) {
            iterator.remove();
        }
    }
}
