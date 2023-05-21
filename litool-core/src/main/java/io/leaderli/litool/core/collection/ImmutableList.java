package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.meta.Lira;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Provide a immutable list
 *
 * @param <T> the type of elements
 * @author leaderli
 * @since 2022/8/11
 */
public class ImmutableList<T> implements Iterable<T> {

    private static final ImmutableList<?> NONE_INSTANCE = new ImmutableList<>(new Object[0]);
    private final int size;
    private final T[] elements;

    @SuppressWarnings("unchecked")
    private ImmutableList(List<T> list) {
        this((T[]) list.toArray());
    }


    private ImmutableList(T[] arr) {
        this.elements = arr;
        this.size = arr.length;
    }

    /**
     * Return ImmutableList consist of elements provide by arr
     *
     * @param arr a arr
     * @param <T> the type of elements in ImmutableList
     * @return new ImmutableList
     */
    @SafeVarargs
    public static <T> ImmutableList<T> of(T... arr) {
        if (arr == null || arr.length == 0) {
            return none();
        }
        return new ImmutableList<>(arr);
    }

    @SuppressWarnings("unchecked")
    public static <T> ImmutableList<T> none() {
        return (ImmutableList<T>) NONE_INSTANCE;
    }

    /**
     * Return ImmutableList consist of elements provide by  iterable
     *
     * @param iterable a iterable provide elements
     * @param <T>      the type of elements in ImmutableList
     * @return new ImmutableList
     */
    @SuppressWarnings("unchecked")
    public static <T> ImmutableList<T> of(Iterable<T> iterable) {
        Lira<T> lira = Lira.of(iterable);
        if (lira.absent()) {
            return none();
        }
        T[] array = ArrayUtils.toArray((Class<T>) Object.class, iterable);
        if (array.length == 0) {
            return none();
        }


        return new ImmutableList<>(ArrayUtils.toArrayWithCommonSuperType(array));

    }

    /**
     * @return the size of  {@link  #elements}
     */
    public int size() {
        return size;
    }

    /**
     * @return the size of {@link  #elements} is {@code  0}
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Return {@code  true} if the element in elements
     *
     * @param element the test element
     * @return the element in elements
     */
    public boolean contains(T element) {
        for (T t : elements) {
            if (Objects.equals(element, t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the element at specified index of elements.
     *
     * @param index the index of elements, Negative numbers are supported, indicating
     *              the position calculated from the back
     * @return the element at specified index of elements
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public T get(int index) {

        if (index < 0) {
            index = size + index;
        }
        return elements[index];
    }


    @Override
    public String toString() {
        return Arrays.toString(elements);
    }


    /**
     * Return the  {@link IterableItr.ArrayItr} if not {@link  #isEmpty()}
     * otherwise return {@link  NoneItr}
     *
     * @return a  iterator
     */
    @Override
    public Iterator<T> iterator() {
        return IterableItr.ofs(elements);
    }

    /**
     * Return a new {@link  java.util.ArrayList} which consist the elements provide
     * by {@link  #elements}
     *
     * @return convert to {@link  java.util.ArrayList}
     */
    public List<T> toList() {
        return CollectionUtils.ofs(elements);
    }
}
