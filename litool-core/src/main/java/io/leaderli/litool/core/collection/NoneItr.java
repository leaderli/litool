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

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        throw new NoSuchElementException();
    }
}
