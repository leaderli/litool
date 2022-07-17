package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

/**
 * @author leaderli
 * @since 2022/7/17
 */
class ArrayIterTest {


    @Test
    void test() {


        ArrayIter<Object> none = new ArrayIter<>(null);

        Assertions.assertFalse(none.hasNext());

        ArrayIter<Integer> iter = ArrayIter.of(1, 2);
        iter.forEachRemaining(s -> {

        });
        Assertions.assertFalse(iter.hasNext());


        iter = ArrayIter.of(1, 2, 3);

        iter.next();
        Assertions.assertTrue(iter.hasNext());

        iter.remove();
        Assertions.assertTrue(iter.hasNext());
        iter.remove();
        Assertions.assertFalse(iter.hasNext());
        iter.remove();
        Assertions.assertFalse(iter.hasNext());

        Assertions.assertThrows(NoSuchElementException.class, iter::next);


    }

}
