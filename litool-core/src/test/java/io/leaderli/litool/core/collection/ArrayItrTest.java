package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

/**
 * @author leaderli
 * @since 2022/7/17
 */
class ArrayItrTest {


    @Test
    void of() {


        IterableItr<?> none = IterableItr.of((Object[]) null);
        Assertions.assertSame(NoneItr.of(), none);
        none = IterableItr.of();
        Assertions.assertSame(NoneItr.of(), none);

        none = IterableItr.of((Object) null);
        Assertions.assertNull(none.next());

        none = IterableItr.of((Object[]) null);
        Assertions.assertSame(NoneItr.of(), none);


    }

    @Test
    void hasNext() {
        IterableItr<Integer> iter = IterableItr.of(1, 2);
        Assertions.assertTrue(iter.hasNext());
        iter.forEachRemaining(s -> {

        });
        Assertions.assertFalse(iter.hasNext());

    }

    @Test
    void next() {

        IterableItr<?> iter = IterableItr.of(1, 2, 3);

        Assertions.assertEquals(1, iter.next());
        Assertions.assertEquals(2, iter.next());
        Assertions.assertEquals(3, iter.next());


        Assertions.assertThrows(NoSuchElementException.class, iter::next);


    }

    @Test
    void remove() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> IterableItr.of().remove());
    }

}
