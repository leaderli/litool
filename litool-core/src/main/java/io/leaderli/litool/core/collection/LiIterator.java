package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.type.LiClassUtil;

import java.lang.reflect.Array;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author leaderli
 * @since 2022/7/17
 */
public class LiIterator<T> implements IterableIter<T>, Enumeration<T> {

    private static final LiIterator<?> NONE = new LiIterator<>(NoneIter.of());


    private final Iterator<T> iterator;


    private LiIterator(Iterator<T> iterator) {

        this.iterator = iterator;
    }


    /**
     * @param obj 将 obj 转换为 LiIterator
     * @param <T> 泛型
     * @return 迭代器
     * @see #of(Iterable)
     * @see #of(Iterator)
     * @see #of(Enumeration)
     * @see #of(Object[])
     */
    @SuppressWarnings("unchecked")
    public static <T> IterableIter<T> of(Object obj) {

        if (obj == null) {
            return NoneIter.of();
        }
        if (obj instanceof Iterator) {
            return of((Iterator<T>) obj);
        }
        if (obj instanceof Iterable) {
            return of((Iterable<T>) obj);
        }
        if (obj instanceof Enumeration) {
            return of((Enumeration<T>) obj);
        }

        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            Object[] objects = LiClassUtil.newArray(obj.getClass().getComponentType(), length);
            for (int i = 0; i < length; i++) {

                objects[i] = Array.get(obj, i);
            }

            return (IterableIter<T>) of(objects);
        }

        return NoneIter.of();

    }

    public static <T> IterableIter<T> of(Iterator<T> iterator) {
        return new LiIterator<>(iterator);
    }

    public static <T> IterableIter<T> of(Iterable<T> iterable) {
        if (iterable == null) {
            return NoneIter.of();
        }
        return new LiIterator<>(iterable.iterator());
    }

    public static <T> IterableIter<T> of(Enumeration<T> enumeration) {
        return new LiIterator<>(EnumerationIter.of(enumeration));
    }

    public static <T> IterableIter<T> of(T[] arr) {
        return new LiIterator<>(ArrayIter.of(arr));
    }


    @Override
    public boolean hasMoreElements() {
        return hasNext();
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

    @Override
    public T nextElement() {
        return next();
    }


}
