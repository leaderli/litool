package io.leaderli.litool.core.collection;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * Make Enumeration behave like  {@link IterableItr}.
 *
 * @param <T> the type of elements returned by the  EnumerationItr
 * @author leaderli
 * @since 2022/7/17
 */
public class EnumerationItr<T> implements IterableItr<T> {


    private final Enumeration<T> enumeration;

    EnumerationItr(Enumeration<T> enumeration) {
        this.enumeration = enumeration;
    }

    /**
     * Returns {@code true} if the enumerationItr has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the enumerationItr has more elements
     */
    @Override
    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }


    /**
     * Returns the next element of this enumeration if this enumeration
     *
     * @return the next element
     * @throws NoSuchElementException if not {@link  #hasNext()}
     */
    @Override
    public T next() {

        if (hasNext()) {
            return enumeration.nextElement();
        }
        throw new NoSuchElementException();

    }


}
