package io.leaderli.litool.core.collection;

import java.util.NoSuchElementException;

/**
 * @param <T> the type of  {@link  IterableItr}
 * @author leaderli
 * @since 2022/7/18
 */
public class NoneItr<T> implements IterableItr<T> {

/**
 * All elementless itr share the same instance
 */
private static final NoneItr<?> NONE = new NoneItr<>();

private NoneItr() {

}

/**
 * Return {@link  #NONE}
 *
 * @param <T> the type of  {@link  IterableItr}
 * @return {@link #NONE}
 */
@SuppressWarnings("unchecked")
public static <T> NoneItr<T> of() {
    return (NoneItr<T>) NONE;
}

/**
 * Return obj is {@link  #NONE}
 *
 * @param obj a obj
 * @return obi is {@link  #NONE}
 */
public static boolean same(Object obj) {
    return NONE == obj;
}

/**
 * @return false
 */
@Override
public boolean hasNext() {
    return false;
}

/**
 * always throw  {@link NoSuchElementException}
 *
 * @return noting
 * @throws NoSuchElementException always throw
 */
@Override
public T next() {
    throw new NoSuchElementException();
}
}
