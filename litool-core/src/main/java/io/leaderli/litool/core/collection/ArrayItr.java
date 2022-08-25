package io.leaderli.litool.core.collection;

import java.util.NoSuchElementException;

/**
 * @author leaderli
 * @since 2022/7/17
 */
public class ArrayItr<T> implements IterableItr<T> {


    private static final ArrayItr<?> NONE = new ArrayItr<>(null);
    private T[] arr;
    private int index = 0;

    private ArrayItr(T[] arr) {

        this.arr = arr;
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <T> ArrayItr<T> of(T... arr) {
        if (arr == null || arr.length == 0) {
            return (ArrayItr<T>) NONE;
        }

        return new ArrayItr<>(arr);
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
