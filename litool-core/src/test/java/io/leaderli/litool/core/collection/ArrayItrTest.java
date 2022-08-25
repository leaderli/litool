package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

/**
 * @author leaderli
 * @since 2022/7/17
 */
class ArrayItrTest {


    @SuppressWarnings("ConfusingArgumentToVarargsMethod")
    @Test
    void test() {


        ArrayItr<?> none = ArrayItr.of((Object[]) null);
        Assertions.assertFalse(none.hasNext());
        none = ArrayItr.of();
        Assertions.assertFalse(none.hasNext());

        none = ArrayItr.of((Object) null);
        Assertions.assertTrue(none.hasNext());

        none = ArrayItr.of(null);
        Assertions.assertFalse(none.hasNext());

        ArrayItr<Integer> iter = ArrayItr.of(1, 2);
        iter.forEachRemaining(s -> {

        });
        Assertions.assertFalse(iter.hasNext());


        iter = ArrayItr.of(1, 2, 3);

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
