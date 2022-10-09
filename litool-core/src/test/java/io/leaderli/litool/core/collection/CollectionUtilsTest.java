package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.Lira;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void toWrapperArray() {
        Assertions.assertNull(CollectionUtils.toArray(null));
        Assertions.assertNull(CollectionUtils.toArray(1));
        Assertions.assertArrayEquals(new Integer[]{1}, CollectionUtils.toArray(new int[]{1}));
        String[] ss = {"1"};
        Assertions.assertNotSame(ss, CollectionUtils.toArray(ss));
        Assertions.assertEquals(Arrays.toString(ss), Arrays.toString(CollectionUtils.toArray(ss)));
        Assertions.assertSame(Boolean.class,
                CollectionUtils.toArray(new boolean[]{}).getClass().getComponentType());
        Assertions.assertSame(Byte.class, CollectionUtils.toArray(new byte[]{}).getClass().getComponentType());
        Assertions.assertSame(Character.class,
                CollectionUtils.toArray(new char[]{}).getClass().getComponentType());
        Assertions.assertSame(Double.class,
                CollectionUtils.toArray(new double[]{}).getClass().getComponentType());
        Assertions.assertSame(Float.class, CollectionUtils.toArray(new float[]{}).getClass().getComponentType());
        Assertions.assertSame(Integer.class, CollectionUtils.toArray(new int[]{}).getClass().getComponentType());
        Assertions.assertSame(Long.class, CollectionUtils.toArray(new long[]{}).getClass().getComponentType());
        Assertions.assertSame(Short.class, CollectionUtils.toArray(new short[]{}).getClass().getComponentType());
        Assertions.assertSame(Object.class,
                CollectionUtils.toArray(new Object[]{}).getClass().getComponentType());
        Assertions.assertSame(String.class,
                CollectionUtils.toArray(new String[]{}).getClass().getComponentType());
        Assertions.assertNull(CollectionUtils.toArray(""));
        Assertions.assertNull(CollectionUtils.toArray(null));
        Assertions.assertSame(2, CollectionUtils.toArray(new int[]{1, 2}).length);
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
        Assertions.assertEquals(1, CollectionUtils.intersection(a, b).size());
        Assertions.assertEquals(0, CollectionUtils.intersection(a, Lira.none()).size());
        Assertions.assertEquals(0, CollectionUtils.intersection(null, null).size());
        Assertions.assertEquals(0, CollectionUtils.intersection(Collections.singletonList(1), null).size());
        Assertions.assertEquals(0, CollectionUtils.intersection(null, Collections.singletonList(1)).size());
    }

    @Test
    void union() {


        Lira<Integer> a = Lira.of(Arrays.asList(1, 2, 3));
        Lira<Integer> b = Lira.of(Arrays.asList(3, 4, 5));

        Assertions.assertEquals(5, CollectionUtils.union(a, b).size());
        Assertions.assertEquals(3, CollectionUtils.union(a, Lira.none()).size());
        Assertions.assertEquals(5, CollectionUtils.union(new Integer[]{1, 2, 3}, new Integer[]{3, 4, 5}).size());
        Assertions.assertEquals(3, CollectionUtils.union(null, new Integer[]{3, 4, 5}).size());
        Assertions.assertEquals(2, CollectionUtils.union(null, Arrays.asList(1, 2)).size());
        Assertions.assertEquals(0, CollectionUtils.union((Iterable<Object>) null, null).size());
    }


    @Test
    void xor() {


        Lira<Integer> a = Lira.of(Arrays.asList(1, 2, 3));
        Lira<Integer> b = Lira.of(Arrays.asList(3, 4, 5));
        Assertions.assertEquals(4, CollectionUtils.xor(a, b).size());
        Assertions.assertEquals(3, CollectionUtils.xor(a, Lira.none()).size());
        b = Lira.of(Arrays.asList(4, 4, 4));
        Assertions.assertEquals(4, CollectionUtils.xor(a, b).size());
        Assertions.assertEquals(3, CollectionUtils.xor(a, null).size());
        Assertions.assertEquals(0, CollectionUtils.xor(null, null).size());


    }

    @Test
    void isEmpty() {

        Assertions.assertTrue(CollectionUtils.isEmpty(null));
        Assertions.assertTrue(CollectionUtils.isEmpty(Collections.emptyList()));
        Assertions.assertFalse(CollectionUtils.isEmpty(Collections.singletonList(1)));
    }

    @Test
    void getDuplicateElements() {
        List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");
        Assertions.assertEquals(2, CollectionUtils.getDuplicateElements(items).size());
    }

    @Test
    void of() {

        Assertions.assertTrue(CollectionUtils.ofs().isEmpty());
        List<Integer> of = CollectionUtils.ofs(1);
        Assertions.assertSame(1, of.size());

        Assertions.assertNotSame(of, CollectionUtils.of(of));
        Assertions.assertNotSame(of, CollectionUtils.of(of.iterator()));
        Assertions.assertEquals(of, CollectionUtils.of(of));
        Assertions.assertEquals(of, CollectionUtils.of(of.iterator()));

    }

    @Test
    void cartesian() {
        Object[][] lists;

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
