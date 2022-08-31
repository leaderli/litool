package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.type.ClassUtil;

import java.util.*;
import java.util.stream.Stream;

/**
 * Provides a synthetic interface with both {@link Iterable} and {@link Iterator}
 *
 * @param <T> the type of elements returned by the iterator
 */
public interface IterableItr<T> extends Iterable<T>, Iterator<T> {


    /**
     * make obj behave like {@link  IterableItr}, support :
     * <ul>
     *     <li>{@link  IterableItr} </li>
     *     <li>{@link  Iterator}</li>
     *     <li>{@link  Iterable}</li>
     *     <li>{@link  Enumeration}</li>
     *     <li>{@link  Map}</li>
     *     <li>Array</li>
     * </ul>
     * <p>
     * otherwise will return {@link  NoneItr}
     *
     * @param obj a obj
     * @param <T> the type of elements {@link  IterableItr} provide
     * @return a {@link  IterableItr}
     */
    @SuppressWarnings("unchecked")
    static <T> IterableItr<T> of(Object obj) {
        if (obj == null) {
            return NoneItr.of();
        }
        T[] arr = null;
        if (obj instanceof Generator) {
            return (IterableItr<T>) obj;
        }
        if (obj instanceof IterableItr) {
            arr = ArrayUtils.toArray(((IterableItr<T>) obj).iterator());
        }
        if (obj instanceof Iterator) {
            arr = ArrayUtils.toArray((Iterator<T>) obj);
        }
        if (obj instanceof Iterable) {
            Iterator<?> iterator = ((Iterable<?>) obj).iterator();
            if (iterator instanceof Generator) {
                return (Generator<T>) iterator;
            }
            arr = ArrayUtils.toArray((Iterator<T>) iterator);
        }
        if (obj instanceof Enumeration) {
            arr = ArrayUtils.toArray((Enumeration<T>) obj);
        }
        if (obj instanceof Stream) {
            arr = ArrayUtils.toArray((Stream<T>) obj);
        }

        if (obj instanceof Map) {

            arr = (T[]) ArrayUtils.toArray(((Map<?, ?>) obj).entrySet());
        }

        if (obj.getClass().isArray()) {

            arr = ClassUtil.toArray(obj);
        }

        return ofs(arr);

    }

    /**
     * Returns an {@link  ArrayItr} over elements of type {@code T}.
     * null or empty array will always return {@link  NoneItr#of()}
     *
     * @param elements an elements
     * @param <T>      the type of elements
     * @return Returns an iterator over elements of type {@code T}.
     */
    @SafeVarargs
    static <T> IterableItr<T> ofs(T... elements) {
        if (elements == null || elements.length == 0) {
            return NoneItr.of();
        }
        return new ArrayItr<>(elements);
    }


    /**
     * Returns {@code true} if the enumerationItr has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the enumerationItr has more elements
     * @see #hasNext()
     */
    default boolean hasMoreElements() {
        return hasNext();
    }

    /**
     * Returns the next element of this enumeration if this enumeration
     *
     * @return the next element
     * @throws NoSuchElementException if not {@link  #hasNext()}
     * @see #next()
     */
    default T nextElement() {
        return next();
    }

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

        @SuppressWarnings("unchecked")
        ArrayItr(List<T> list) {
            this.arr = (T[]) list.toArray();
        }

        @Override
        public Iterator<T> iterator() {
            return new ArrayItr<>(arr);
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
}
