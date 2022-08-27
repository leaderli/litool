package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.type.ClassUtil;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Provides a synthetic interface with both {@link Iterable} and {@link Iterator}
 *
 * @param <T> the type of elements returned by the iterator
 */
public interface IterableItr<T> extends Iterable<T>, Iterator<T> {


    @Override
    default Iterator<T> iterator() {
        return this;
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
     * make obj behave like {@link  IterableItr}, support :
     * <ul>
     *     <li>{@link  IterableItr} </li>
     *     <li>{@link  IterableItr}</li>
     *     <li>{@link  Iterator}</li>
     *     <li>{@link  Iterable}</li>
     *     <li>{@link  Enumeration}</li>
     *     <li>Array</li>
     * </ul>
     * <p>
     * otherwise will return {@link  NoneItr}
     *
     * @param obj a obj
     * @param <T> the type of elements {@link  IterableItr} provide
     * @return a {@link  IterableItr}
     * @see #of(Iterator)
     * @see #of(Iterable)
     * @see #of(Enumeration)
     * @see #ofs(Object[])
     */
    @SuppressWarnings("unchecked")
    static <T> IterableItr<T> of(Object obj) {
        if (obj == null) {
            return NoneItr.of();
        }
        if (obj instanceof IterableItr) {
            return (IterableItr<T>) obj;
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

        if (obj instanceof Map) {

            return (IterableItr<T>) of((Map<?,?>) obj);
        }

        if (obj.getClass().isArray()) {

            return ofs(ClassUtil.toArray(obj));
        }

        return NoneItr.of();
    }

    /**
     * Returns an {@link  EnumerationItr} over elements of type {@code T}.
     * null or empty enumeration will always return {@link  NoneItr#of()}
     *
     * @param enumeration an enumeration
     * @param <T>         the type of elements that enumeration provide
     * @return Returns an iterator over the elements that enumeration provide of type {@code T}.
     */
    static <T> IterableItr<T> of(Enumeration<T> enumeration) {

        if (enumeration == null || !enumeration.hasMoreElements()) {
            return NoneItr.of();
        }
        return new EnumerationItr<>(enumeration);
    }

    /**
     * Returns an {@link  IteratorDelegate} over elements of type {@code T}.
     * null or empty iterator will always return {@link  NoneItr#of()}
     *
     * @param iterator an iterator
     * @param <T>      the type of elements that iterator provide
     * @return Returns an LiIterator over the elements that iterator provide of type {@code T}.
     */
    static <T> IterableItr<T> of(Iterator<T> iterator) {

        if (iterator == null || !iterator.hasNext()) {
            return NoneItr.of();
        }
        return new IteratorDelegate<>(iterator);
    }

    /**
     * Returns an {@link  IteratorDelegate} over elements of type {@code T}.
     * null or empty iterable will always return {@link  NoneItr#of()}
     *
     * @param iterable an iterable
     * @param <T>      the type of elements that iterable provide
     * @return Returns an LiIterator over the elements that iterable provide of type {@code T}.
     */
    static <T> IterableItr<T> of(Iterable<T> iterable) {

        if (iterable == null) {
            return NoneItr.of();
        }
        return of(iterable.iterator());
    }

    /**
     * Returns an {@link  IteratorDelegate} over map of type {@code K,V}.
     * null or empty map will always return {@link  NoneItr#of()}
     *
     * @param map an  map
     * @param <K> the type of  map key
     * @param <V> the type of  map value
     * @return Returns an LiIterator over the entry that map provide of type {@code K,V}.
     */
    static <K, V> IterableItr<Map.Entry<K, V>> of(Map<K, V> map) {

        if (map == null || map.isEmpty()) {
            return NoneItr.of();
        }
        return of(map.entrySet());
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
}
