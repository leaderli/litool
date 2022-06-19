package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LiLimitArrayTest {


    @Test
    public void test() {
        Assertions.assertThrows(NegativeArraySizeException.class, () ->
                new LiLimitArray<>(-1)
        );

    }

    @Test
    public void test1() {
        LiLimitArray<Integer> limitArray = new LiLimitArray<>(10);

        limitArray.add(1);
        limitArray.add(2);

        assertTrue(limitArray.contains(1));
        assertTrue(limitArray.contains(2));

        limitArray.remove(2);

        assertTrue(limitArray.contains(1));
        assertFalse(limitArray.contains(2));

        for (int i = 0; i < 10; i++) {
            limitArray.add(i);
        }


        for (int i = 0; i < 10; i++) {
            limitArray.contains(i);
        }

        limitArray.add(4);
        limitArray.remove(4);
        limitArray.remove(3);
        assertFalse(limitArray.contains(3));
        assertFalse(limitArray.contains(4));

    }


}
