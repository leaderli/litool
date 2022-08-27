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
public class IteratorDelegate<T> implements IterableItr<T> {


private final Iterator<T> delegate;


IteratorDelegate(Iterator<T> delegate) {

    this.delegate = delegate;
}


@Override
public boolean hasNext() {
    return delegate.hasNext();
}

@Override
public T next() {

    if (hasNext()) {
        return delegate.next();
    }
    throw new NoSuchElementException();
}

@Override
public void remove() {
    delegate.remove();
}

}
