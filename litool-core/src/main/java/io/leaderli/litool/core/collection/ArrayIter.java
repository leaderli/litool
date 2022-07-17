package io.leaderli.litool.core.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author leaderli
 * @since 2022/7/17
 */
public class ArrayIter<T> implements Iterator<T> {


    private T[] arr;
    private int index = 0;

    public ArrayIter(T[] arr) {
        this.arr = arr;
    }

    @SafeVarargs
    public static <T> ArrayIter<T> of(T... arr) {


        return new ArrayIter<>(arr);
    }

    @Override
    public boolean hasNext() {
        return arr != null && index < arr.length;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return arr[index++];
    }

    @SuppressWarnings("unchecked")
    @Override
    public void remove() {
        if (hasNext()) {

            T[] newArr = (T[]) new Object[arr.length - 1];
            System.arraycopy(arr, 0, newArr, 0, index);
            System.arraycopy(arr, index + 1, newArr, index, arr.length - (index + 1));
            this.arr = newArr;
        }

    }
}
