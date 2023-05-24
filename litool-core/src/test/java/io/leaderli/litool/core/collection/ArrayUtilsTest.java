package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.type.ClassUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author leaderli
 * @since 2022/7/26 9:09 AM
 */
@SuppressWarnings("ConstantConditions")
class ArrayUtilsTest {


    @Test
    void convertToTargetArray() {

        Assertions.assertThrows(ArrayStoreException.class, () -> ArrayUtils.convertToTargetArray(Integer.class, 1, 2, true));

        Assertions.assertEquals(1, ArrayUtils.convertToTargetArray(Integer.class, (Object) null).length);
    }

    @Test
    void arraycopy() {


        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.arraycopy("1"));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.arraycopy(null));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.arraycopy(new Object()));
        assertDoesNotThrow(() -> ArrayUtils.arraycopy(new Object[0]));

        int[] intArray = new int[]{1, 2, 3};
        int[] intArrayCopy = ArrayUtils.arraycopy(intArray);
        assertArrayEquals(intArray, intArrayCopy);
        assertNotSame(intArray, intArrayCopy);

        String[] strArray = new String[]{"a", "b", "c"};
        String[] strArrayCopy = ArrayUtils.arraycopy(strArray);
        assertArrayEquals(strArray, strArrayCopy);

        Object[] objArray = new Object[]{1, "a", true};
        Object[] objArrayCopy = ArrayUtils.arraycopy(objArray);
        assertArrayEquals(objArray, objArrayCopy);

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
        Object[] append = ArrayUtils.append(null, add);
        assertNotSame(add, append);
        assertArrayEquals(add, append);

        Object[] newAppend = ArrayUtils.append(append, (Object[]) null);
        assertNotSame(newAppend, append);
        assertArrayEquals(newAppend, append);

        Integer[] array1 = {1, 2, 3};
        Integer[] array2 = {4, 5, 6};
        Integer[] expected = {1, 2, 3, 4, 5, 6};
        assertArrayEquals(expected, ArrayUtils.append(array1, array2));

        Integer[] array3 = {};
        Integer[] array4 = {1, 2, 3};
        Integer[] expected2 = {1, 2, 3};
        assertArrayEquals(expected2, ArrayUtils.append(array3, array4));

        Integer[] array5 = {1};
        Integer[] array6 = {};
        Integer[] expected3 = {1};
        assertArrayEquals(expected3, ArrayUtils.append(array5, array6));

        String[] array7 = {"a", "b", "c"};
        String[] array8 = {"d", "e", "f"};
        String[] expected4 = {"a", "b", "c", "d", "e", "f"};
        assertArrayEquals(expected4, ArrayUtils.append(array7, array8));

        List<?>[] lists = new List[0];
        assertEquals(2, ArrayUtils.append(lists, Arrays.asList(1, 2), Arrays.asList("a", "b")).length);

    }

    @Test
    void isArray() {
        assertFalse(ArrayUtils.isArray(null));
        assertFalse(ArrayUtils.isArray(1));
        assertFalse(ArrayUtils.isArray("string"));

        assertTrue(ArrayUtils.isArray(new int[]{}));
        assertTrue(ArrayUtils.isArray(new Integer[]{}));
        assertTrue(ArrayUtils.isArray(new Integer[][]{}));


        assertTrue(ArrayUtils.isArray(new int[]{1, 2, 3}));
        assertTrue(ArrayUtils.isArray(new String[]{"a", "b", "c"}));
        assertTrue(ArrayUtils.isArray(new Object[]{1, "a", true}));
    }

    @Test
    void insert() {

        assertEquals("[1]", Arrays.toString(ArrayUtils.append(null, 1)));
        assertEquals("[1]", Arrays.toString(ArrayUtils.insert(null, 1, 1)));
        assertEquals("[1]", Arrays.toString(ArrayUtils.insert(new Integer[]{1}, 1)));
        assertEquals(0, ArrayUtils.insert(null, 1).length);
        assertEquals("[1, 2, 3, 4]", Arrays.toString(ArrayUtils.insert(new Integer[]{1, 2, 3}, 4, 4)));
        assertEquals("[1, 2, 3, 4]", Arrays.toString(ArrayUtils.insert(new Integer[]{1, 2, 3}, 3, 4)));
        assertEquals("[1, 2, 4, 3]", Arrays.toString(ArrayUtils.insert(new Integer[]{1, 2, 3}, 2, 4)));
        assertEquals("[4, 1, 2, 3]", Arrays.toString(ArrayUtils.insert(new Integer[]{1, 2, 3}, 0, 4)));
        assertEquals("[1, 2, 4, 3]", Arrays.toString(ArrayUtils.insert(new Integer[]{1, 2, 3}, -1, 4)));
        assertEquals("[4, 1, 2, 3]", Arrays.toString(ArrayUtils.insert(new Integer[]{1, 2, 3}, -3, 4)));
        assertEquals("[4, 1, 2, 3]", Arrays.toString(ArrayUtils.insert(new Integer[]{1, 2, 3}, -4, 4)));


        Integer[] array1 = {1, 3, 4};
        Integer[] expected1 = {1, 2, 3, 4};
        assertArrayEquals(expected1, ArrayUtils.insert(array1, 1, 2));

        Integer[] array2 = {1, 2, 3};
        Integer[] expected2 = {1, 2, 3, 4};
        assertArrayEquals(expected2, ArrayUtils.insert(array2, 3, 4));

        Integer[] array3 = {1, 2, 3};
        Integer[] expected3 = {1, 2, 4, 3};
        assertArrayEquals(expected3, ArrayUtils.insert(array3, -1, 4));

        Integer[] array4 = {};
        Integer[] expected4 = {1, 2, 3};
        assertArrayEquals(expected4, ArrayUtils.insert(array4, 0, 1, 2, 3));

        Integer[] array5 = {1, 2, 3};
        Integer[] expected5 = {1, 2, 3};
        assertArrayEquals(expected5, ArrayUtils.insert(array5, 3));

        Integer[] array6 = {1, 2, 3};
        Integer[] expected6 = {1, 2, 3};
        assertArrayEquals(expected6, ArrayUtils.insert(array6, 0));

        Integer[] array7 = null;
        Integer[] expected7 = {1, 2, 3};
        assertArrayEquals(expected7, ArrayUtils.insert(array7, 0, 1, 2, 3));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void sub() {

        Object[] original = null;

        assertNull(ArrayUtils.subArray(original, 1, 2));
        original = new String[]{};
        assertEquals(0, ArrayUtils.subArray(original, 1, 2).length);
        original = new String[]{"1", "2", "3"};

        assertEquals(0, ArrayUtils.subArray(original, 2, 2).length);
        assertEquals(0, ArrayUtils.subArray(original, 2, 1).length);
        assertEquals(0, ArrayUtils.subArray(original, 0, -4).length);
        assertEquals(3, ArrayUtils.subArray(original, -5, 0).length);
        assertEquals(0, ArrayUtils.subArray(original, 5, 0).length);
        assertEquals(3, ArrayUtils.subArray(original, 0, 10).length);

        assertEquals("3", ArrayUtils.subArray(original, 2, 3)[0]);
        assertEquals("3", ArrayUtils.subArray(original, 2, 0)[0]);
        assertEquals("2", ArrayUtils.subArray(original, 1, -1)[0]);
        assertEquals("3", ArrayUtils.subArray(original, -1, 0)[0]);
        assertEquals("2", ArrayUtils.subArray(original, -2, 0)[0]);

        assertEquals(2, ArrayUtils.subArray(new int[]{1, 2, 3}, -2, 0)[0]);


        Integer[] intArray = {1, 2, 3, 4, 5};
        Integer[] sub1 = ArrayUtils.subArray(intArray, 0, 3);
        Integer[] sub2 = ArrayUtils.subArray(intArray, 1, 4);
        Integer[] sub3 = ArrayUtils.subArray(intArray, -2, -1);
        Integer[] sub4 = ArrayUtils.subArray(intArray, 0, 0);
        Integer[] sub5 = ArrayUtils.subArray(intArray, 5, 7);
        Integer[] sub6 = ArrayUtils.subArray(intArray, -3, 2);
        assertArrayEquals(intArray, ArrayUtils.subArray(intArray, 0, 5));
        assertArrayEquals(new Integer[]{1, 2, 3}, sub1);
        assertArrayEquals(new Integer[]{2, 3, 4}, sub2);
        assertArrayEquals(new Integer[]{4}, sub3);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, sub4);
        assertArrayEquals(new Integer[]{}, sub5);
        assertArrayEquals(new Integer[]{}, sub6);

    }

    @Test
    void of() {
        Integer[] integers = {1, 2, 3};
        Integer[] expectedIntegers = {1, 2, 3};
        assertArrayEquals(expectedIntegers, ArrayUtils.of(integers));

        String[] strings = {"a", "b", "c"};
        String[] expectedStrings = {"a", "b", "c"};
        assertArrayEquals(expectedStrings, ArrayUtils.of(strings));

        Integer[] emptyIntegers = {};
        Integer[] expectedEmptyIntegers = {};
        assertArrayEquals(expectedEmptyIntegers, ArrayUtils.of(emptyIntegers));

        String[] emptyStrings = {};
        String[] expectedEmptyStrings = {};
        assertArrayEquals(expectedEmptyStrings, ArrayUtils.of(emptyStrings));

        int[] primitiveInts = {1, 2, 3};
        Integer[] expectedWrapperIntegers = {1, 2, 3};
        assertArrayEquals(expectedWrapperIntegers, ArrayUtils.of(1, 2, 3));


    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void removeSub() {

        Object[] original;

        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.removeSub(null, 1, 2));
        original = new String[]{};
        assertEquals(0, ArrayUtils.removeSub(original, 1, 2).length);
        original = new String[]{"1", "2", "3"};

        assertEquals(3, ArrayUtils.removeSub(original, 2, 2).length);
        assertEquals(3, ArrayUtils.removeSub(original, 2, 1).length);
        assertEquals(3, ArrayUtils.removeSub(original, 0, -4).length);
        assertEquals(0, ArrayUtils.removeSub(original, -5, 0).length);
        assertEquals(3, ArrayUtils.removeSub(original, 5, 0).length);
        assertEquals(0, ArrayUtils.removeSub(original, 0, 10).length);

        assertEquals("1", ArrayUtils.removeSub(original, 2, 3)[0]);
        assertEquals("1", ArrayUtils.removeSub(original, 2, 0)[0]);
        assertEquals("1", ArrayUtils.removeSub(original, 1, -1)[0]);
        assertEquals("1", ArrayUtils.removeSub(original, -1, 0)[0]);
        assertEquals("2", ArrayUtils.removeSub(original, -1, 0)[1]);
        assertEquals("1", ArrayUtils.removeSub(original, -2, 0)[0]);
        assertEquals(3, ArrayUtils.removeSub(original, 4, 0).length);

        assertEquals(1, ArrayUtils.removeSub(new int[]{1, 2, 3}, -2, 0)[0]);
        assertArrayEquals(new int[]{2, 3}, ArrayUtils.removeSub(new int[]{1, 2, 3}, 0, 1));
        assertArrayEquals(new int[]{}, ArrayUtils.removeSub(new int[]{}, 0, 1));

        // Test removing empty subset
        Integer[] arr1 = {1, 2, 3, 4, 5};
        assertArrayEquals(new Integer[]{}, ArrayUtils.removeSub(arr1, 0, 0));

        // Test removing subset from beginning
        Integer[] arr2 = {1, 2, 3, 4, 5};
        assertArrayEquals(new Integer[]{1}, ArrayUtils.removeSub(arr2, 1, 5));

        // Test removing subset from end
        Integer[] arr3 = {1, 2, 3, 4, 5};
        assertArrayEquals(new Integer[]{5}, ArrayUtils.removeSub(arr3, 0, 4));

        // Test removing subset that covers the entire array
        Integer[] arr4 = {1, 2, 3, 4, 5};
        assertArrayEquals(new Integer[0], ArrayUtils.removeSub(arr4, 0, 5));

        // Test removing subset with negative endIndex
        Integer[] arr6 = {1, 2, 3, 4, 5};
        assertArrayEquals(new Integer[]{5}, ArrayUtils.removeSub(arr6, 0, -1));

        // Test removing subset with negative beginIndex and endIndex
        Integer[] arr7 = {1, 2, 3, 4, 5};
        assertArrayEquals(new Integer[]{5}, ArrayUtils.removeSub(arr7, -5, -1));

        // Test removing from empty array
        Integer[] arr8 = {};
        assertArrayEquals(new Integer[0], ArrayUtils.removeSub(arr8, 0, 0));

        // Test removing from null array
        Integer[] arr9 = null;
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.removeSub(null, 1, 2));

    }

    @Test
    void testToString() {

        assertEquals("null", ArrayUtils.toString(null));
        assertEquals("1", ArrayUtils.toString(1));
        assertEquals("[]", ArrayUtils.toString(new Object[]{}));
        assertEquals("[1, 2]", ArrayUtils.toString(new int[]{1, 2}));
        assertEquals("[[1, 2]]", ArrayUtils.toString(new int[][]{{1, 2}}));
        assertEquals("[[1, 2], 3]", ArrayUtils.toString(new Object[]{new Object[]{1, 2}, 3}));

    }


    @Test
    void toArray() {

        Integer[] actual = ArrayUtils.toArray(Integer.class, Arrays.asList(1, 2).iterator());
        assertArrayEquals(new Integer[]{1, 2}, actual);

        actual = ArrayUtils.toArray(Integer.class, Stream.of(1, 2));
        assertArrayEquals(new Integer[]{1, 2}, actual);

        actual = ArrayUtils.toArray(Integer.class, Arrays.asList(1, 2));
        assertArrayEquals(new Integer[]{1, 2}, actual);

        actual = ArrayUtils.toArray(Integer.class, Stream.of(1, 2));
        assertArrayEquals(new Integer[]{1, 2}, actual);

        actual = ArrayUtils.toArray(Integer.class, IterableItr.ofs(1, 2).enumeration());
        assertArrayEquals(new Integer[]{1, 2}, actual);

        List<Integer> iterable = Collections.emptyList();
        actual = ArrayUtils.toArray(Integer.class, iterable);
        assertArrayEquals(new Integer[]{}, actual);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void toWrapperArray() {
        Assertions.assertNull(ArrayUtils.toArray(null));
        Assertions.assertNull(ArrayUtils.toArray(1));
        Assertions.assertNull(ArrayUtils.toArray(""));
        Assertions.assertArrayEquals(new Integer[]{1, null}, ArrayUtils.toArray(new Integer[]{1, null}));
        Assertions.assertArrayEquals(new Integer[]{1}, ArrayUtils.toArray(new int[]{1}));

        String[] ss = {"1"};
        Assertions.assertNotSame(ss, ArrayUtils.toArray(ss));
        Assertions.assertEquals(Arrays.toString(ss), Arrays.toString(ArrayUtils.toArray(ss)));
        Assertions.assertSame(Boolean.class, ArrayUtils.toArray(new boolean[]{}).getClass().getComponentType());
        Assertions.assertSame(Byte.class, ArrayUtils.toArray(new byte[]{}).getClass().getComponentType());
        Assertions.assertSame(Character.class, ArrayUtils.toArray(new char[]{}).getClass().getComponentType());
        Assertions.assertSame(Double.class, ArrayUtils.toArray(new double[]{}).getClass().getComponentType());
        Assertions.assertSame(Float.class, ArrayUtils.toArray(new float[]{}).getClass().getComponentType());
        Assertions.assertSame(Integer.class, ArrayUtils.toArray(new int[]{}).getClass().getComponentType());
        Assertions.assertSame(Long.class, ArrayUtils.toArray(new long[]{}).getClass().getComponentType());
        Assertions.assertSame(Short.class, ArrayUtils.toArray(new short[]{}).getClass().getComponentType());
        Assertions.assertSame(Object.class, ArrayUtils.toArray(new Object[]{}).getClass().getComponentType());
        Assertions.assertSame(String.class, ArrayUtils.toArray(new String[]{}).getClass().getComponentType());

        Assertions.assertSame(2, ArrayUtils.toArray(new int[]{1, 2}).length);
    }

}
