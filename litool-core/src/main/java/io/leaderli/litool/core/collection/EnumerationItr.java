package io.leaderli.litool.core.collection;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * @author leaderli
 * @since 2022/7/17
 */
public class EnumerationItr<T> implements IterableItr<T>, Enumeration<T> {


    private final Enumeration<T> enumeration;

    public EnumerationItr(Enumeration<T> enumeration) {
        this.enumeration = enumeration;
    }


    public static <T> EnumerationItr<T> of(Enumeration<T> enumeration) {


        return new EnumerationItr<>(enumeration);
    }

    @Override
    public boolean hasNext() {
        return hasMoreElements();
    }

    @Override
    public boolean hasMoreElements() {
        return enumeration != null && enumeration.hasMoreElements();
    }

    @Override
    public T nextElement() {


        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return enumeration.nextElement();
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


}
