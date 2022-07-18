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
class LiIteratorTest {


    @Test
    void of() {

        Assertions.assertFalse(LiIterator.of((Iterable<Object>) null).hasNext());
        Assertions.assertFalse(LiIterator.of((Iterator<Object>) null).hasNext());


        Assertions.assertFalse(LiIterator.of((Object[]) null).hasNext());
        Assertions.assertThrows(NoSuchElementException.class, LiIterator.of((Iterable<Object>) null)::next);


        Assertions.assertTrue(LiIterator.of(Arrays.asList(1, 2)).hasNext());
        Assertions.assertTrue(LiIterator.of(1).hasNext());
        Assertions.assertEquals(1, LiIterator.of(Arrays.asList(1, 2)).next());
        Assertions.assertThrows(IllegalStateException.class, LiIterator.of(Arrays.asList(1, 2))::remove);


    }
}
