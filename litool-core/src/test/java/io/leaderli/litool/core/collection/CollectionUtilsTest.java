package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.meta.Lira;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/1/22
 */
public class CollectionUtilsTest {

    @Test

    public void test1() {
        List<String> items =
                Arrays.asList("apple", "apple", "banana",
                        "apple", "orange", "banana", "papaya");

        Assertions.assertEquals("[apple, apple, banana]", CollectionUtils.getDuplicateElement(items).toString());

    }


    @Test
    public void testCartesianProduct() {
        List<List<Object>> lists = CollectionUtils.cartesianProduct(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
        Assertions.assertEquals(lists.toString(), "[[1, 4], [1, 5], [1, 6], [2, 4], [2, 5], [2, 6], [3, 4], [3, 5], [3, 6]]");

        List<List<Object>> lists2 = CollectionUtils.cartesianProduct(Arrays.asList(1, 2), Arrays.asList("-", "*"), Arrays.asList("a", "b"));
        Assertions.assertEquals("[[1, -, a], [1, -, b], [1, *, a], [1, *, b], [2, -, a], [2, -, b], [2, *, a], [2, *, b]]", lists2.toString());
    }

    @Test
    public void toWrapperArray() {
        Assertions.assertNull(CollectionUtils.toWrapperArray(null));
        Assertions.assertNull(CollectionUtils.toWrapperArray(1));
        Assertions.assertSame(Integer[].class, CollectionUtils.toWrapperArray(new int[]{1}).getClass());
        Assertions.assertSame(String[].class, CollectionUtils.toWrapperArray(new String[]{"1"}).getClass());
        String[] ss = {"1"};
        Assertions.assertNotSame(ss, CollectionUtils.toWrapperArray(ss));
        Assertions.assertEquals(Arrays.toString(ss), Arrays.toString(CollectionUtils.toWrapperArray(ss)));

    }

    @Test
   public void emptyList() {
        List<Object> actual = CollectionUtils.emptyList();
        Assertions.assertEquals(0, actual.size());
        actual.add(0);
        Assertions.assertEquals(1, actual.size());
        actual.remove(0);
        Assertions.assertEquals(0, actual.size());

    }


    @Test
    public void intersection() {


        Lira<Integer> a = Lira.of(Arrays.asList(1, 2, 3));
        Lira<Integer> b = Lira.of(Arrays.asList(3, 4, 5));
        Assertions.assertEquals(1, CollectionUtils.intersection(a, b).size());
        Assertions.assertEquals(0, CollectionUtils.intersection(a, Lira.none()).size());
    }

    @Test
    public void union() {


        Lira<Integer> a = Lira.of(Arrays.asList(1, 2, 3));
        Lira<Integer> b = Lira.of(Arrays.asList(3, 4, 5));
        Assertions.assertEquals(5, CollectionUtils.union(a, b).size());
        Assertions.assertEquals(3, CollectionUtils.union(a, Lira.none()).size());
        Assertions.assertEquals(5, CollectionUtils.union(new Integer[]{1, 2, 3}, new Integer[]{3, 4, 5}).size());
        Assertions.assertEquals(3, CollectionUtils.union(null,new Integer[]{3, 4, 5}).size());
    }


    @Test
    public void xor() {


        Lira<Integer> a = Lira.of(Arrays.asList(1, 2, 3));
        Lira<Integer> b = Lira.of(Arrays.asList(3, 4, 5));
        Assertions.assertEquals(4, CollectionUtils.xor(a, b).size());
        Assertions.assertEquals(3, CollectionUtils.xor(a, Lira.none()).size());


    }
}
