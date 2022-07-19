package io.leaderli.litool.core.collection;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * @author leaderli
 * @since 2022/7/17
 */
public class EnumerationIter<T> implements IterableIter<T>, Enumeration<T> {


    private final Enumeration<T> enumeration;

    public EnumerationIter(Enumeration<T> enumeration) {
        this.enumeration = enumeration;
    }


    public static <T> EnumerationIter<T> of(Enumeration<T> enumeration) {


        return new EnumerationIter<>(enumeration);
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

    @Override
    public T next() {

        return nextElement();

    }


}
