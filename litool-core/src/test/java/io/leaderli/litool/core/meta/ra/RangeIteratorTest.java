package io.leaderli.litool.core.meta.ra;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author leaderli
 * @since 2023/6/19 1:13 PM
 */
class RangeIteratorTest {

    @Test
    void test() {

        RangeIterator rangeIterator = new RangeIterator(0, 1);

        Assertions.assertTrue(rangeIterator.hasNext());
        assertEquals(0, rangeIterator.next());
        Assertions.assertFalse(rangeIterator.hasNext());
        assertThrows(NoSuchElementException.class, rangeIterator::next);

    }

}
