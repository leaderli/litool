package io.leaderli.litool.core.collection;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * @author leaderli
 * @since 2022/7/17
 */
public class EnumerationItr<T> implements IterableItr<T>, Enumeration<T> {


    private final Enumeration<T> enumeration;

    EnumerationItr(Enumeration<T> enumeration) {
        this.enumeration = enumeration;
    }

    @Override
    public boolean hasNext() {
        return hasMoreElements();
    }

    @Override
    public boolean hasMoreElements() {
        return enumeration.hasMoreElements();
    }

    @Override
    public T nextElement() {


        if (hasNext()) {
            return enumeration.nextElement();
        }
        throw new NoSuchElementException();

    }

    /**
     * Returns the next element of this enumeration if this enumeration
     *
     * @return the next element
     * @throws NoSuchElementException if not {@link  #hasNext()}
     */
    @Override
    public T next() {

        return nextElement();

    }
    static <T> EnumerationItr<T> of(Enumeration<T> enumeration) {


        return new EnumerationItr<>(enumeration);
    }

}
