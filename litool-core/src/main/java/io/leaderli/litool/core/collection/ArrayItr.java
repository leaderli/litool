package io.leaderli.litool.core.collection;

import java.util.*;

/**
 * ArrayItr 是一个将数组转化为 IterableItr 的实现类。
 *
 * @param <T> ArrayItr 迭代器中元素的类型
 * @author leaderli
 * @since 2022/7/17
 */
public class ArrayItr<T> implements IterableItr<T> {


    protected final T[] arr;
    /**
     * The index of current {@code ArrayItr}
     */
    private int index = 0;

    ArrayItr(T[] arr) {
        this.arr = arr;
    }

    @SuppressWarnings("unchecked")
    ArrayItr(List<T> list) {
        this.arr = (T[]) list.toArray();
    }

    @Override
    public Iterator<T> iterator() {
        return new io.leaderli.litool.core.collection.ArrayItr<>(arr);
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


    @Override
    public String name() {
        return "ArrayItr";
    }

    @Override
    public ArrayList<T> toList() {
        ArrayList<T> list = new ArrayList<>();
        Collections.addAll(list, arr);
        return list;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof io.leaderli.litool.core.collection.ArrayItr) {
            return Arrays.equals(arr, ((io.leaderli.litool.core.collection.ArrayItr<?>) obj).arr);
        }
        return false;
    }

    @Override
    public T[] toArray(Class<T> type) {
        return ArrayUtils.toArray(type, arr);
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(arr, arr.length);
    }


}
