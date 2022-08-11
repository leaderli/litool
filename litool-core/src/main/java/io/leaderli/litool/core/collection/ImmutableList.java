package io.leaderli.litool.core.collection;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/8/11
 */
public class ImmutableList<T> {

    private static ImmutableList<?> NONE = new ImmutableList<>(new Object[0]);
    private final int size;
    private final T[] elements;

    @SuppressWarnings("unchecked")
    private ImmutableList(List<T> list) {
        this.elements = (T[]) list.toArray();
        this.size = list.size();
    }


    private ImmutableList(T[] arr) {
        this.elements = arr;
        this.size = arr.length;
    }

    @SuppressWarnings("unchecked")
    public static <T> ImmutableList<T> none() {
        return (ImmutableList<T>) NONE;
    }

    @SafeVarargs
    public static <T> ImmutableList<T> of(T... arr) {
        if (arr == null || arr.length == 0) {
            return none();
        }
        return new ImmutableList<>(arr);
    }

    public static <T> ImmutableList<T> of(List<T> list) {
        if (list == null || list.isEmpty()) {
            return none();
        }
        return new ImmutableList<>(list);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size > 0;
    }

    public boolean contains(T element) {
        for (T t : elements) {
            if (Objects.equals(element, t)) {
                return true;
            }
        }
        return false;
    }

    public T get(int index) {
        return elements[index];
    }


    @Override
    public String toString() {
        return Arrays.toString(elements);
    }
}
