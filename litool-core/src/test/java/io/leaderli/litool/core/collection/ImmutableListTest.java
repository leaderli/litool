package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

/**
 * @author leaderli
 * @since 2022/8/12
 */
class ImmutableListTest {


    @Test
    void of() {

        Assertions.assertSame(ImmutableList.none(), ImmutableList.of((Object[]) null));
        Assertions.assertSame(ImmutableList.none(), ImmutableList.of(Collections.emptyList()));
    }


    @Test
    void size() {
        Assertions.assertSame(0, ImmutableList.none().size());
        Assertions.assertSame(2, ImmutableList.of(1, 2).size());
    }

    @Test
    void isEmpty() {

        Assertions.assertSame(true, ImmutableList.none().isEmpty());
        Assertions.assertSame(false, ImmutableList.of(1, 2).isEmpty());
    }

    @Test
    void contains() {

        Assertions.assertTrue(ImmutableList.of(1, 2).contains(1));
        Assertions.assertTrue(ImmutableList.of(null, 2).contains(null));
        Assertions.assertFalse(ImmutableList.of(1, 2).contains(3));
        Assertions.assertFalse(ImmutableList.of(1, 2).contains(null));
    }

    @Test
    void get() {
        Assertions.assertSame(1, ImmutableList.of(1, 2).get(0));
        Assertions.assertSame(1, ImmutableList.of(1, 2).get(-2));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> ImmutableList.of(1, 2).get(2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> ImmutableList.of(1, 2).get(-3));
    }


    @Test
    void iterator() {
        Assertions.assertSame(NoneItr.of(), ImmutableList.none().iterator());
        Assertions.assertSame(ArrayItr.class, ImmutableList.of(1).iterator().getClass());
    }

    @Test
    void toList() {


        Assertions.assertTrue(ImmutableList.none().toList().isEmpty());
        Assertions.assertEquals("[1, 2]", ImmutableList.of(1, 2).toList().toString());

    }
}
