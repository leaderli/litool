package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * @author leaderli
 * @since 2022/8/12
 */
class ImmutableListTest {


    @Test
    void of() {

        Assertions.assertSame(ImmutableList.none(), ImmutableList.of((Object[]) null));
        Assertions.assertSame(ImmutableList.none(), ImmutableList.of(Collections.emptyList()));
        Assertions.assertSame(ImmutableList.none(), ImmutableList.of(Stream.empty()));
        Assertions.assertSame(ImmutableList.none(), ImmutableList.of(Collections.emptyIterator()));
        String[] array = ImmutableList.<String>of().toArray(String.class);

        Assertions.assertEquals(ImmutableList.of(1, 2), ImmutableList.of(1, 2));
        Assertions.assertEquals(ImmutableList.of(1, 2), ImmutableList.of(Arrays.asList(1, 2)));
        Assertions.assertEquals(ImmutableList.of(1, 2), ImmutableList.of(Arrays.asList(1, 2).iterator()));
        Assertions.assertEquals(ImmutableList.of(1, 2), ImmutableList.of(IterableItr.of(Arrays.asList(1, 2))));
        Assertions.assertEquals(ImmutableList.of(1, 2), ImmutableList.of(Stream.of(1, 2)));

        Integer[] arr = new Integer[]{1, 2};
        ImmutableList<Integer> of = ImmutableList.of(arr);
        arr[0] = 10;
        Assertions.assertEquals(1, of.get(0));
        Assertions.assertEquals(2, of.get(1));
        Assertions.assertEquals(2, of.size());


        ImmutableList<Number> numbers = ImmutableList.of(1, 1.0, 2f, 3L);
        for (Number number : numbers) {
            Assertions.assertTrue(number.longValue() > 0);
        }
        Number[] numbersArray = numbers.toArray(Number.class);
        Assertions.assertSame(Number[].class, numbersArray.getClass());

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
        Assertions.assertFalse(ImmutableList.of().contains(null));
    }

    @Test
    void get() {
        Assertions.assertSame(1, ImmutableList.of(1, 2).get(0));
        Assertions.assertSame(1, ImmutableList.of(1, 2).get(-2));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> ImmutableList.of(1, 2).get(2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> ImmutableList.of(1, 2).get(-3));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> ImmutableList.of().get(-3));
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

    @Test
    void toArray() {

        ImmutableList<Integer> immutableList = ImmutableList.none();
        Assertions.assertEquals(0, immutableList.toArray(int.class).length);
        immutableList = ImmutableList.of(1, 2);
        Assertions.assertEquals(2, immutableList.toArray(int.class).length);
        Assertions.assertEquals(1, immutableList.toArray(int.class)[0]);

    }
}
