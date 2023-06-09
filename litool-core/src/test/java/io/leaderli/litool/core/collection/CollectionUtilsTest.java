package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.Lira;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author leaderli
 * @since 2022/1/22
 */
class CollectionUtilsTest {


    @Test
    void tuple() {

        String[] s1 = new String[]{"a", "b"};
        Integer[] s2 = new Integer[]{1, 2};

        Assertions.assertEquals(Lira.of(LiTuple.of("a", 1), LiTuple.of("b", 2)), CollectionUtils.tuple(s1, s2));
        Assertions.assertThrows(NullPointerException.class, () -> CollectionUtils.tuple(s1, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> CollectionUtils.tuple(s1, new Object[]{}));
        Assertions.assertTrue(CollectionUtils.tuple(new Object[]{}, new Object[]{}).absent());
    }


    @Test
    void emptyList() {
        List<Object> actual = CollectionUtils.emptyList();
        Assertions.assertEquals(0, actual.size());
        actual.add(0);
        Assertions.assertEquals(1, actual.size());
        actual.remove(0);
        Assertions.assertEquals(0, actual.size());
        Assertions.assertTrue(CollectionUtils.emptyList().isEmpty());

    }


    @Test
    void intersection() {


        Lira<Integer> a = Lira.of(Arrays.asList(1, 2, 3));
        Lira<Integer> b = Lira.of(Arrays.asList(3, 4, 5));
        Assertions.assertEquals(1, CollectionUtils.intersection(Integer.class, a, b).size());
        Assertions.assertEquals(0, CollectionUtils.intersection(Integer.class, a, Lira.none()).size());
        Assertions.assertEquals(0, CollectionUtils.intersection(Object.class, (Object[]) null, null).size());
        Assertions.assertEquals(0, CollectionUtils.intersection(Integer.class, Collections.singletonList(1), null).size());
        Assertions.assertEquals(0, CollectionUtils.intersection(Integer.class, null, Collections.singletonList(1)).size());

        Assertions.assertArrayEquals(new Integer[]{1, 3, 2}, CollectionUtils.intersection(Integer.class, new Integer[]{1, 3, 2}, new Integer[]{3, 1, 2}).toArray(Integer.class));
        Assertions.assertArrayEquals(new Integer[]{1, 2}, CollectionUtils.intersection(Integer.class, new Integer[]{1, 3, 5, 2}, new Integer[]{2, 4, 1}).toArray(Integer.class));
    }

    @Test
    void union() {


        Lira<Integer> a = Lira.of(Arrays.asList(1, 2, 3));
        Lira<Integer> b = Lira.of(Arrays.asList(3, 4, 5));

        Assertions.assertEquals(5, CollectionUtils.union(Integer.class, a, b).size());

        Assertions.assertEquals(3, CollectionUtils.union(Integer.class, a, Lira.none()).size());
        Assertions.assertEquals(5, CollectionUtils.union(Integer.class, new Integer[]{1, 2, 3}, new Integer[]{3, 4, 5}).size());
        Assertions.assertEquals(3, CollectionUtils.union(Integer.class, null, new Integer[]{3, 4, 5}).size());
        Assertions.assertEquals(2, CollectionUtils.union(Integer.class, null, Arrays.asList(1, 2)).size());
        Assertions.assertEquals(0, CollectionUtils.union(Object.class, (Iterable<Object>) null, null).size());

        Assertions.assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, CollectionUtils.union(Integer.class, new Integer[]{1, 2, 3}, new Integer[]{3, 4, 5}).toArray(Integer.class));
        Assertions.assertArrayEquals(new Integer[]{1, 3, 5, 2, 4}, CollectionUtils.union(Integer.class, new Integer[]{1, 3, 5}, new Integer[]{2, 4, 1}).toArray(Integer.class));
    }


    @Test
    void xor() {


        Lira<Integer> a = Lira.of(Arrays.asList(1, 2, 3));
        Lira<Integer> b = Lira.of(Arrays.asList(3, 4, 5));
        Assertions.assertEquals(4, CollectionUtils.xor(Integer.class, a, b).size());
        Assertions.assertEquals(3, CollectionUtils.xor(Integer.class, a, Lira.none()).size());
        b = Lira.of(Arrays.asList(4, 4, 4));
        Assertions.assertEquals(4, CollectionUtils.xor(Integer.class, a, b).size());
        Assertions.assertEquals(3, CollectionUtils.xor(Integer.class, a, null).size());
        Assertions.assertEquals(0, CollectionUtils.xor(Integer.class, null, null).size());


    }


    @Test
    void getDuplicateElements() {
        Assertions.assertEquals(0, CollectionUtils.getDuplicateElements(null).size());
        Assertions.assertEquals(0, CollectionUtils.getDuplicateElements(Collections.EMPTY_LIST).size());
        Assertions.assertEquals(0, CollectionUtils.getDuplicateElements(Arrays.asList(1, 2, 3, 4, 5)).size());
        Assertions.assertEquals(2, CollectionUtils.getDuplicateElements(Arrays.asList(1, 2, 2, 3, 3, 3)).size());
    }

    @Test
    void toList() {

        Assertions.assertTrue(CollectionUtils.toList().isEmpty());
        List<Integer> of = CollectionUtils.toList(1);
        Assertions.assertSame(1, of.size());
    }

    @Test
    void toSet() {

        Assertions.assertTrue(CollectionUtils.toSet().isEmpty());
        Set<Integer> of = CollectionUtils.toSet(1);
        Assertions.assertSame(1, of.size());
        of = CollectionUtils.toSet(1, 1);
        Assertions.assertSame(1, of.size());
    }


    @Test
    void test() {

        CollectionUtils.cartesian(ArrayUtils.of(2, 1, 3), ArrayUtils.of("-", "*"), ArrayUtils.of("a", "b"));

    }

    @Test
    void cartesian() {
        Object[][] lists;

        lists = CollectionUtils.cartesian();
        Assertions.assertEquals("[]", ArrayUtils.toString(lists));

        lists = CollectionUtils.cartesian((Object[][]) null);
        Assertions.assertEquals("[]", ArrayUtils.toString(lists));

        lists = CollectionUtils.cartesian((Object[]) null);
        Assertions.assertEquals("[]", ArrayUtils.toString(lists));

        lists = CollectionUtils.cartesian(null, new Object[]{1, 2});
        Assertions.assertEquals("[]", ArrayUtils.toString(lists));

        lists = CollectionUtils.cartesian(new Object[]{1, 2}, null);
        Assertions.assertEquals("[]", ArrayUtils.toString(lists));

        lists = CollectionUtils.cartesian(new Object[]{}, new Object[]{1, 2});
        Assertions.assertEquals("[]", ArrayUtils.toString(lists));

        lists = CollectionUtils.cartesian(new Object[]{null}, new Object[]{1, 2});
        Assertions.assertEquals("[[null, 1], [null, 2]]", ArrayUtils.toString(lists));

        lists = CollectionUtils.cartesian(ArrayUtils.of(1, 2, 3), ArrayUtils.of(4, 5, 6));
        Assertions.assertEquals("[[1, 4], [1, 5], [1, 6], [2, 4], [2, 5], [2, 6], [3, 4], [3, 5], [3, 6]]",
                ArrayUtils.toString(lists));
        lists = CollectionUtils.cartesian(ArrayUtils.of(1, 2), ArrayUtils.of("-", "*"), ArrayUtils.of("a", "b"));
        Assertions.assertEquals("[[1, -, a], [1, -, b], [1, *, a], [1, *, b], [2, -, a], [2, -, b], [2, *, a], [2, *," +
                " " +
                "b]]", ArrayUtils.toString(lists));
        lists = CollectionUtils.cartesian(ArrayUtils.of(1, 2));
        Assertions.assertEquals("[[1], [2]]", ArrayUtils.toString(lists));

        lists = CollectionUtils.cartesian(new Object[]{null});
        Assertions.assertEquals("[[null]]", ArrayUtils.toString(lists));
        lists = CollectionUtils.cartesian(ArrayUtils.of(1, 1), ArrayUtils.of(1, 1));
        Assertions.assertEquals(1, lists.length);


        lists = CollectionUtils.cartesian(ArrayUtils.of(1, 2), ArrayUtils.of(null, 3));
        Assertions.assertEquals(4, lists.length);
    }


}
