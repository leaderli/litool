package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.meta.Lino;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author leaderli
 * @since 2022/7/17
 */
public class LiIterator<T> implements Iterator<T> {


    private final Iterator<T> iterator;

    public LiIterator(Iterable<T> iterable) {
        this(Lino.of(iterable).map(Iterable::iterator).get());
    }

    public LiIterator(Iterator<T> iterator) {

        this.iterator = iterator;
    }

    @SafeVarargs
    public LiIterator(T... arr) {
        this.iterator = ArrayIter.of(arr);
    }

    public static <T> LiIterator<T> of(Iterator<T> iterator) {
        return new LiIterator<>(iterator);
    }

    public static <T> LiIterator<T> of(Iterable<T> iterable) {
        return new LiIterator<>(iterable);
    }

    @SafeVarargs
    public static <T> LiIterator<T> of(T... arr) {
        return new LiIterator<>(arr);
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
