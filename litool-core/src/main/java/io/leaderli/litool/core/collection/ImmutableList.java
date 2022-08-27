package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.meta.Lira;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/8/11
 */
public class ImmutableList<T> implements Iterable<T> {

    private static final ImmutableList<?> NONE_INSTANCE = new ImmutableList<>(new Object[0]);
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
        return (ImmutableList<T>) NONE_INSTANCE;
    }

    @SafeVarargs
    public static <T> ImmutableList<T> of(T... arr) {
        if (arr == null || arr.length == 0) {
            return none();
        }
        return new ImmutableList<>(arr);
    }

    @SuppressWarnings("unchecked")
    public static <T> ImmutableList<T> of(Iterable<T> iterable) {
        Lira<T> lira = Lira.of(iterable);
        if (lira.absent()) {
            return none();
        }
        return new ImmutableList<>((T[]) lira.toArray());

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

    @Override
    public Iterator<T> iterator() {
        return IterableItr.ofs(elements);
    }

    public List<T> copy() {
        return CollectionUtils.of(elements);
    }
}
