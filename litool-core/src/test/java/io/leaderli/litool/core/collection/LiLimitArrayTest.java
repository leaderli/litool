package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LiLimitArrayTest {


    @Test
    void add() {
        Assertions.assertThrows(NegativeArraySizeException.class, () -> new LiLimitArray<>(-1));
        LiLimitArray<Integer> limit = new LiLimitArray<>(0);
        Assertions.assertFalse(limit.contains(1));
        limit.add(1);
        Assertions.assertFalse(limit.contains(1));

        limit = new LiLimitArray<>(1);
        limit.add(1);
        Assertions.assertTrue(limit.contains(1));

        limit = new LiLimitArray<>(0);
        limit.add(1);
        Assertions.assertFalse(limit.contains(1));
    }

    @Test
    void contains() {
        LiLimitArray<Integer> limit = new LiLimitArray<>(1);
        limit.add(1);
        Assertions.assertTrue(limit.contains(1));
        limit.add(2);
        Assertions.assertFalse(limit.contains(1));
        Assertions.assertTrue(limit.contains(2));

        limit = new LiLimitArray<>(1);
        limit.add(1);
        Assertions.assertTrue(limit.contains(1));
    }

    @Test
    void remove() {

        LiLimitArray<Integer> limit = new LiLimitArray<>(0);
        Assertions.assertFalse(limit.remove(1));

        limit = new LiLimitArray<>(1);
        limit.add(1);
        Assertions.assertFalse(limit.remove(null));
        Assertions.assertTrue(limit.remove(1));
        Assertions.assertFalse(limit.remove(2));

        limit = new LiLimitArray<>(0);
        Assertions.assertFalse(limit.remove(null));
        Assertions.assertFalse(limit.remove(1));
    }

    @Test
    void toArray() {

        LiLimitArray<Integer> limit = new LiLimitArray<>(1);
        Assertions.assertEquals(1, limit.toArray(Integer.class).length);
        limit = new LiLimitArray<>(0);
        Assertions.assertEquals(0, limit.toArray(Integer.class).length);

    }


}
