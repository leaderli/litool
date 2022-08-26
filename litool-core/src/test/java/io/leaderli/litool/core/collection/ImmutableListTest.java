package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author leaderli
 * @since 2022/8/12
 */
class ImmutableListTest {


    @Test
    void test() {
        Assertions.assertSame(ImmutableList.none(), ImmutableList.of((Object[]) null));
        Assertions.assertSame(ImmutableList.none(), ImmutableList.of(Collections.emptyList()));

        ImmutableList<Integer> list = ImmutableList.of(Arrays.asList(1, 2));

        Assertions.assertEquals(1, list.get(0));
        Assertions.assertEquals(2, list.get(1));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> list.get(2));

        Assertions.assertTrue(list.contains(1));
        Assertions.assertTrue(list.contains(2));
        Assertions.assertFalse(list.contains(3));


        ImmutableList<Integer> of = ImmutableList.of(1, 2, 3);
        of.iterator().next();
        Assertions.assertEquals(3, of.get(2));
        for (Integer integer : of) {
            Assertions.assertTrue(integer < 4);
        }
    }

}
