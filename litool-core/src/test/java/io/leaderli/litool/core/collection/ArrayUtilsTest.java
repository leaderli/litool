package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.exception.AssertException;
import io.leaderli.litool.core.type.ClassUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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

        Assertions.assertArrayEquals(a, c);

        Assertions.assertThrows(NullPointerException.class, () -> ArrayUtils.arraycopy(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ArrayUtils.arraycopy("1"));


    }

    @Test
    void combination() {

        int[] a = new int[]{1};
        int[] b = new int[]{2};

        Assertions.assertEquals("[1, 2]", Arrays.toString(ArrayUtils.combination(a, b)));
        Assertions.assertArrayEquals(new int[]{1, 2}, ArrayUtils.combination(a, b));

        a = null;
        Assertions.assertNotSame(b, ArrayUtils.combination(a, b));
        Assertions.assertArrayEquals(b, ArrayUtils.combination(a, b));


        a = new int[]{1};
        b = null;
        Assertions.assertNotSame(a, ArrayUtils.combination(a, b));
        Assertions.assertArrayEquals(a, ArrayUtils.combination(a, b));

        Assertions.assertThrows(AssertException.class, () -> ArrayUtils.combination(null, null));


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

        Assertions.assertArrayEquals(new Integer[]{1, 2}, ArrayUtils.toArray(Arrays.asList(1, 2)));
    }
}
