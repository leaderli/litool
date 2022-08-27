package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * @author leaderli
 * @since 2022/8/27
 */
class EnumerationItrTest {
    EnumerationItr<Integer> itr;

    @BeforeEach
    public void before() {

        Vector<Integer> vector = new Vector<>(1);
        vector.add(1);
        Enumeration<Integer> elements = vector.elements();
        itr = EnumerationItr.of(elements);
    }

    @Test
    void hasNext() {
        Assertions.assertTrue(itr.hasNext());
        itr.next();
        Assertions.assertFalse(itr.hasNext());
    }

    @Test
    void hasMoreElements() {
        Assertions.assertTrue(itr.hasMoreElements());
        itr.next();
        Assertions.assertFalse(itr.hasMoreElements());
    }

    @Test
    void nextElement() {
        itr.nextElement();
        Assertions.assertThrows(NoSuchElementException.class, () -> itr.nextElement());

    }

    @Test
    void next() {
        itr.next();
        Assertions.assertThrows(NoSuchElementException.class, () -> itr.next());
    }
}
