package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LiFixedSizeSetTest {


    @Test
    void add() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LiFixedSizeSet<>(-1));
        LiFixedSizeSet<Integer> limit;
        limit = new LiFixedSizeSet<>(1);
        limit.add(1);
        assertTrue(limit.contains(1));
        limit.add(2);
        assertFalse(limit.contains(1));

        limit = new LiFixedSizeSet<>(2);
        limit.add(1);
        limit.add(2);
        Assertions.assertEquals(2, limit.iterator().next());
        limit.add(1);
        Assertions.assertEquals(2, limit.iterator().next());

        limit = new LiFixedSizeSet<>(2, true);
        limit.add(1);
        limit.add(2);
        Assertions.assertEquals(2, limit.iterator().next());
        limit.add(1);
        Assertions.assertEquals(1, limit.iterator().next());
    }

    @Test
    void contains() {
        LiFixedSizeSet<Integer> limit = new LiFixedSizeSet<>(1);
        limit.add(1);
        assertTrue(limit.contains(1));
        limit.add(2);
        assertFalse(limit.contains(1));
        assertTrue(limit.contains(2));

        limit = new LiFixedSizeSet<>(1);
        limit.add(1);
        assertTrue(limit.contains(1));
    }

    @Test
    void remove() {

        LiFixedSizeSet<Integer> limit = new LiFixedSizeSet<>(1);
        limit.add(1);
        assertFalse(limit.remove(null));
        assertTrue(limit.remove(1));
        assertFalse(limit.remove(2));


    }

    @Test
    void toArray() {

        LiFixedSizeSet<Integer> limit = new LiFixedSizeSet<>(1);
        Assertions.assertEquals(0, limit.toArray(Integer.class).length);
        limit.add(1);
        Assertions.assertEquals(1, limit.toArray(Integer.class).length);
        limit.add(2);
        Assertions.assertEquals(1, limit.toArray(Integer.class).length);

    }

    @Test
    void toList() {
        LiFixedSizeSet<Integer> limit = new LiFixedSizeSet<>(1);
        assertTrue(limit.toList().isEmpty());
        limit.add(1);
        assertFalse(limit.toList().isEmpty());

    }

    @Test
    void forEach() {

        LiFixedSizeSet<Integer> fix = new LiFixedSizeSet<>(10);

        for (Integer f : fix) {

            System.out.println(f);
        }
    }

}
