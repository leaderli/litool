package io.leaderli.litool.core.collection;

import java.util.NoSuchElementException;

/**
 * Make arrays behave like  {@link IterableItr}.
 *
 * @param <T> the type of elements returned by the ArrayItr
 * @author leaderli
 * @since 2022/7/17
 */
class ArrayItr<T> implements IterableItr<T> {


    private final T[] arr;
    /**
     * The index of current {@code ArrayItr}
     */
    private int index = 0;

    ArrayItr(T[] arr) {
        this.arr = arr;
    }


    @Override
    public boolean hasNext() {
        return index < arr.length;
    }

    @Override
    public T next() {
        if (hasNext()) {
            return arr[index++];
        }
        throw new NoSuchElementException();
    }

}
