package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author leaderli
 * @since 2022/7/17
 */
class IterableIterTest {


    @Test
    void of() {

        Assertions.assertFalse(IterableIter.of((Iterable<Object>) null).hasNext());
        Assertions.assertFalse(IterableIter.of((Iterator<Object>) null).hasNext());
        Assertions.assertThrows(NoSuchElementException.class, IterableIter.of((Iterable<Object>) null)::next);


        Assertions.assertTrue(IterableIter.of(Arrays.asList(1, 2)).hasNext());
        Assertions.assertEquals(1, IterableIter.of(Arrays.asList(1, 2)).next());
        Assertions.assertThrows(IllegalStateException.class, IterableIter.of(Arrays.asList(1, 2))::remove);


    }
}
