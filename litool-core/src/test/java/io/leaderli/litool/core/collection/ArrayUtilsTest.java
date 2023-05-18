package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.type.ClassUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author leaderli
 * @since 2022/7/26 9:09 AM
 */
@SuppressWarnings("ConstantConditions")
class ArrayUtilsTest {


    @Test
    void arraycopy() {

        long[] a = new long[]{1};

        long[] c = ArrayUtils.arraycopy(a);

        assertArrayEquals(a, c);

        assertThrows(NullPointerException.class, () -> ArrayUtils.arraycopy(null));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.arraycopy("1"));


    }

    @Test
    void combineArrays() {
        // 测试组合两个非空 int 数组
        int[] a1 = {1, 2, 3};
        int[] b1 = {4, 5, 6};
        int[] expected1 = {1, 2, 3, 4, 5, 6};
        assertArrayEquals(expected1, ArrayUtils.combineArrays(a1, b1));

        // 测试组合一个非空 Integer 数组和一个空 int 数组
        Integer[] a3 = {1, 2, 3};
        Integer[] b3 = {};
        Integer[] expected3 = {1, 2, 3};
        assertArrayEquals(expected3, ArrayUtils.combineArrays(a3, b3));

        // 测试组合两个 null 数组
        int[] a4 = null;
        Integer[] b4 = null;
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.combineArrays(a4, b4));

        // 测试组合一个 null int 数组和一个非 null Integer 数组
        int[] a5 = null;
        Integer[] b5 = {4, 5, 6};
        Integer[] expected5 = {4, 5, 6};
        assertArrayEquals(expected5, (Integer[]) ArrayUtils.combineArrays(a5, b5));

        // 测试组合一个非 null Integer 数组和一个 null int 数组
        Integer[] a6 = {1, 2, 3};
        int[] b6 = null;
        Integer[] expected6 = {1, 2, 3};
        assertArrayEquals(expected6, (Integer[]) ArrayUtils.combineArrays(a6, b6));

        // 测试组合一个非数组和一个数组
        Integer a7 = 1;
        Integer[] b7 = {4, 5, 6};
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.combineArrays(a7, b7));

        // 测试组合一个数组和一个非数组
        int[] a8 = {1, 2, 3};
        Integer b8 = 4;
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.combineArrays(a8, b8));

        // 测试组合两个不同类型的数组
        Integer[] a9 = {1, 2, 3};
        String[] b9 = {"hello", "world"};
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.combineArrays(a9, b9));

        // 测试组合两个不同维数的数组
        int[][] a10 = {{1, 2}, {3, 4}};
        int[] b10 = {5, 6};
        Object[] expected10 = {new int[]{1, 2}, new int[]{3, 4}, new int[]{5, 6}};
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.combineArrays(a10, b10));

        // 测试组合 null int 数组和非 null int 数组
        int[] a11 = null;
        int[] b11 = {1, 2};
        int[] expected11 = {1, 2};
        assertArrayEquals(expected11, ArrayUtils.combineArrays(a11, b11));

        // 测试组合非 null int 数组和 null int 数组
        int[] a12 = {1, 2};
        int[] b12 = null;
        int[] expected12 = {1, 2};
        assertArrayEquals(expected12, ArrayUtils.combineArrays(a12, b12));

        // 测试组合两个空 int 数组
        int[] a13 = {};
        int[] b13 = {};
        int[] expected13 = {};
        assertArrayEquals(expected13, ArrayUtils.combineArrays(a13, b13));

        // 测试组合两个非空 Integer 数组
        Integer[] a14 = {1, 2, 3};
        Integer[] b14 = {4, 5, 6};
        Integer[] expected14 = {1, 2, 3, 4, 5, 6};
        assertArrayEquals(expected14, ArrayUtils.combineArrays(a14, b14));
    }


    @Test
    void add() {

        Object[] add = ClassUtil.newWrapperArray(Object.class, 0);
        Object[] append = ArrayUtils.add(null, add);
        Assertions.assertNotSame(add, append);
        Assertions.assertArrayEquals(add, append);

        Object[] newAppend = ArrayUtils.add(append, (Object[]) null);
        Assertions.assertNotSame(newAppend, append);
        Assertions.assertArrayEquals(newAppend, append);
    }

    @Test
    void isArray() {

        Assertions.assertFalse(ArrayUtils.isArray(null));
        Assertions.assertFalse(ArrayUtils.isArray(1));
        Assertions.assertTrue(ArrayUtils.isArray(new int[]{}));
        Assertions.assertTrue(ArrayUtils.isArray(new Integer[]{}));
        Assertions.assertTrue(ArrayUtils.isArray(new Integer[][]{}));
    }

    @Test
    void insert() {

        Assertions.assertEquals("[1]", Arrays.toString(ArrayUtils.add(null, 1)));
        Assertions.assertEquals("[1]", Arrays.toString(ArrayUtils.insert(null, 1, 1)));
        Assertions.assertEquals("[1]", Arrays.toString(ArrayUtils.insert(new Integer[]{1}, 1)));
        Assertions.assertEquals(0, ArrayUtils.insert(null, 1).length);
        Assertions.assertEquals("[1, 2, 3, 4]", Arrays.toString(ArrayUtils.insert(new Integer[]{1, 2, 3}, 4, 4)));
        Assertions.assertEquals("[1, 2, 3, 4]", Arrays.toString(ArrayUtils.insert(new Integer[]{1, 2, 3}, 3, 4)));
        Assertions.assertEquals("[1, 2, 4, 3]", Arrays.toString(ArrayUtils.insert(new Integer[]{1, 2, 3}, 2, 4)));
        Assertions.assertEquals("[4, 1, 2, 3]", Arrays.toString(ArrayUtils.insert(new Integer[]{1, 2, 3}, 0, 4)));
        Assertions.assertEquals("[1, 2, 4, 3]", Arrays.toString(ArrayUtils.insert(new Integer[]{1, 2, 3}, -1, 4)));
        Assertions.assertEquals("[4, 1, 2, 3]", Arrays.toString(ArrayUtils.insert(new Integer[]{1, 2, 3}, -3, 4)));
        Assertions.assertEquals("[4, 1, 2, 3]", Arrays.toString(ArrayUtils.insert(new Integer[]{1, 2, 3}, -4, 4)));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void sub() {

        Object[] original = null;

        Assertions.assertNull(ArrayUtils.subArray(original, 1, 2));
        original = new String[]{};
        Assertions.assertEquals(0, ArrayUtils.subArray(original, 1, 2).length);
        original = new String[]{"1", "2", "3"};

        Assertions.assertEquals(0, ArrayUtils.subArray(original, 2, 2).length);
        Assertions.assertEquals(0, ArrayUtils.subArray(original, 2, 1).length);
        Assertions.assertEquals(0, ArrayUtils.subArray(original, 0, -4).length);
        Assertions.assertEquals(3, ArrayUtils.subArray(original, -5, 0).length);
        Assertions.assertEquals(0, ArrayUtils.subArray(original, 5, 0).length);
        Assertions.assertEquals(3, ArrayUtils.subArray(original, 0, 10).length);

        Assertions.assertEquals("3", ArrayUtils.subArray(original, 2, 3)[0]);
        Assertions.assertEquals("3", ArrayUtils.subArray(original, 2, 0)[0]);
        Assertions.assertEquals("2", ArrayUtils.subArray(original, 1, -1)[0]);
        Assertions.assertEquals("3", ArrayUtils.subArray(original, -1, 0)[0]);
        Assertions.assertEquals("2", ArrayUtils.subArray(original, -2, 0)[0]);

        Assertions.assertEquals(2, ArrayUtils.subArray(new int[]{1, 2, 3}, -2, 0)[0]);


    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void remove() {

        Object[] original = null;

        Assertions.assertNull(ArrayUtils.remove(original, 1, 2));
        original = new String[]{};
        Assertions.assertEquals(0, ArrayUtils.remove(original, 1, 2).length);
        original = new String[]{"1", "2", "3"};

        Assertions.assertEquals(3, ArrayUtils.remove(original, 2, 2).length);
        Assertions.assertEquals(3, ArrayUtils.remove(original, 2, 1).length);
        Assertions.assertEquals(3, ArrayUtils.remove(original, 0, -4).length);
        Assertions.assertEquals(0, ArrayUtils.remove(original, -5, 0).length);
        Assertions.assertEquals(3, ArrayUtils.remove(original, 5, 0).length);
        Assertions.assertEquals(0, ArrayUtils.remove(original, 0, 10).length);

        Assertions.assertEquals("1", ArrayUtils.remove(original, 2, 3)[0]);
        Assertions.assertEquals("1", ArrayUtils.remove(original, 2, 0)[0]);
        Assertions.assertEquals("1", ArrayUtils.remove(original, 1, -1)[0]);
        Assertions.assertEquals("1", ArrayUtils.remove(original, -1, 0)[0]);
        Assertions.assertEquals("2", ArrayUtils.remove(original, -1, 0)[1]);
        Assertions.assertEquals("1", ArrayUtils.remove(original, -2, 0)[0]);
        Assertions.assertEquals(3, ArrayUtils.remove(original, 4, 0).length);

        Assertions.assertEquals(1, ArrayUtils.remove(new int[]{1, 2, 3}, -2, 0)[0]);
        Assertions.assertArrayEquals(new int[]{2, 3}, ArrayUtils.remove(new int[]{1, 2, 3}, 0, 1));
        Assertions.assertArrayEquals(new int[]{}, ArrayUtils.remove(new int[]{}, 0, 1));


    }

    @Test
    void testToString() {

        Assertions.assertEquals("null", ArrayUtils.toString(null));
        Assertions.assertEquals("[]", ArrayUtils.toString(new Object[]{}));
        Assertions.assertEquals("[1, 2]", ArrayUtils.toString(new int[]{1, 2}));
        Assertions.assertEquals("[[1, 2]]", ArrayUtils.toString(new int[][]{{1, 2}}));

    }

    @Test
    void toArray() {

        Assertions.assertArrayEquals(new Object[]{null, null}, ArrayUtils.toArray(null, null));
        Number[] objects = ArrayUtils.toArray(1, 1.0);

        Integer[] actual = ArrayUtils.toArray(Arrays.asList(1, 2).iterator());
        Assertions.assertArrayEquals(new Integer[]{1, 2}, actual);

        actual = ArrayUtils.toArray(Stream.of(1, 2));
        Assertions.assertArrayEquals(new Integer[]{1, 2}, actual);

        actual = ArrayUtils.toArray(Arrays.asList(1, 2));
        Assertions.assertArrayEquals(new Integer[]{1, 2}, actual);

        Integer[][] lists = ArrayUtils.toArray(new Integer[]{1, 2}, new Integer[]{3, 4});
        Assertions.assertArrayEquals(new Integer[][]{new Integer[]{1, 2}, new Integer[]{3, 4}}, lists);


    }
}
