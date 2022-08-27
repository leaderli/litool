package io.leaderli.litool.core.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Make Iterator behave like  {@link IterableItr}.
 *
 * @param <T> the type of elements returned by the iterator
 * @author leaderli
 * @since 2022/7/17
 */
public class IteratorProxy<T> implements IterableItr<T> {


    private final Iterator<T> iterator;


    IteratorProxy(Iterator<T> iterator) {

        this.iterator = iterator;
    }


    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {

        if (hasNext()) {
            return iterator.next();
        }
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

}
