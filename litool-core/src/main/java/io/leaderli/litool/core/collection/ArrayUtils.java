
package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.type.ClassUtil;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * <p>Operations on arrays, primitive arrays (like {@code int[]}) and
 * primitive wrapper arrays (like {@code Integer[]}).
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will not be thrown for a {@code null}
 * array input. However, an Object array that contains a {@code null}
 * element may throw an exception. Each method documents its behaviour.
 *
 * <p>#ThreadSafe#
 *
 * @since 2.0
 */
public class ArrayUtils {
    /**
     * An empty immutable {@code Object} array.
     */
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    /**
     * An empty immutable {@code Class} array.
     */
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    /**
     * An empty immutable {@code String} array.
     */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    /**
     * An empty immutable {@code long} array.
     */
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    /**
     * An empty immutable {@code Long} array.
     */
    public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
    /**
     * An empty immutable {@code int} array.
     */
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    /**
     * An empty immutable {@code Integer} array.
     */
    public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
    /**
     * An empty immutable {@code short} array.
     */
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    /**
     * An empty immutable {@code Short} array.
     */
    public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
    /**
     * An empty immutable {@code byte} array.
     */
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    /**
     * An empty immutable {@code Byte} array.
     */
    public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
    /**
     * An empty immutable {@code double} array.
     */
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    /**
     * An empty immutable {@code Double} array.
     */
    public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
    /**
     * An empty immutable {@code float} array.
     */
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    /**
     * An empty immutable {@code Float} array.
     */
    public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
    /**
     * An empty immutable {@code boolean} array.
     */
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    /**
     * An empty immutable {@code Boolean} array.
     */
    public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
    /**
     * An empty immutable {@code char} array.
     */
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
    /**
     * An empty immutable {@code Character} array.
     */
    public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
// ----------------------------------------------------------------------

    /**
     * <p>Checks if an array of Objects is not empty and not {@code null}.
     *
     * @param <T>   the component type of the array
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     * @since 2.5
     */
    public static <T> boolean isNotEmpty(final T[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>Checks if an array of Objects is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final Object[] array) {
        return getLength(array) == 0;
    }

    /**
     * <p>Returns the length of the specified array.
     * This method can deal with {@code Object} arrays and with primitive arrays.
     *
     * <p>If the input array is {@code null}, {@code 0} is returned.
     *
     * <pre>
     * ArrayUtils.getLength(null)            = 0
     * ArrayUtils.getLength([])              = 0
     * ArrayUtils.getLength([null])          = 1
     * ArrayUtils.getLength([true, false])   = 2
     * ArrayUtils.getLength([1, 2, 3])       = 3
     * ArrayUtils.getLength(["a", "b", "c"]) = 3
     * </pre>
     *
     * @param array the array to retrieve the length from, may be null
     * @return The length of the array, or {@code 0} if the array is {@code null}
     * @throws IllegalArgumentException if the object argument is not an array.
     * @since 2.1
     */
    public static int getLength(final Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }

    /**
     * <p>Checks if an array of primitive longs is not empty and not {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     * @since 2.5
     */
    public static boolean isNotEmpty(final long[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>Checks if an array of primitive longs is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final long[] array) {
        return getLength(array) == 0;
    }

    /**
     * <p>Checks if an array of primitive ints is not empty and not {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     * @since 2.5
     */
    public static boolean isNotEmpty(final int[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>Checks if an array of primitive ints is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final int[] array) {
        return getLength(array) == 0;
    }

    /**
     * <p>Checks if an array of primitive shorts is not empty and not {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     * @since 2.5
     */
    public static boolean isNotEmpty(final short[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>Checks if an array of primitive shorts is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final short[] array) {
        return getLength(array) == 0;
    }

// ----------------------------------------------------------------------

    /**
     * <p>Checks if an array of primitive chars is not empty and not {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     * @since 2.5
     */
    public static boolean isNotEmpty(final char[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>Checks if an array of primitive chars is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final char[] array) {
        return getLength(array) == 0;
    }

    /**
     * <p>Checks if an array of primitive bytes is not empty and not {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     * @since 2.5
     */
    public static boolean isNotEmpty(final byte[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>Checks if an array of primitive bytes is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final byte[] array) {
        return getLength(array) == 0;
    }

    /**
     * <p>Checks if an array of primitive doubles is not empty and not {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     * @since 2.5
     */
    public static boolean isNotEmpty(final double[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>Checks if an array of primitive doubles is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final double[] array) {
        return getLength(array) == 0;
    }

    /**
     * <p>Checks if an array of primitive floats is not empty and not {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     * @since 2.5
     */
    public static boolean isNotEmpty(final float[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>Checks if an array of primitive floats is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final float[] array) {
        return getLength(array) == 0;
    }

    /**
     * <p>Checks if an array of primitive booleans is not empty and not {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     * @since 2.5
     */
    public static boolean isNotEmpty(final boolean[] array) {
        return !isEmpty(array);
    }

//-----------------------------------------------------------------------

    /**
     * <p>Checks if an array of primitive booleans is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final boolean[] array) {
        return getLength(array) == 0;
    }

// Reverse
//-----------------------------------------------------------------------

    /**
     * <p>Reverses the order of the given array.
     *
     * <p>There is no special handling for multi-dimensional arrays.
     *
     * <p>This method does nothing for a {@code null} input array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final Object[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * <p>
     * Reverses the order of the given array in the given range.
     *
     * <p>
     * This method does nothing for a {@code null} input array.
     *
     * @param array               the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index. Under value (&lt;0) is promoted to 0, over value (&gt;array
     *                            .length)
     *                            results in no
     *                            change.
     * @param endIndexExclusive   elements up to endIndex-1 are reversed in the array. Under value (&lt; start index)
     *                            results in no
     *                            change. Over value (&gt;array.length) is demoted to array length.
     * @since 3.2
     */
    public static void reverse(final Object[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        Object tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * <p>Reverses the order of the given array.
     *
     * <p>This method does nothing for a {@code null} input array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final long[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * <p>
     * Reverses the order of the given array in the given range.
     *
     * <p>
     * This method does nothing for a {@code null} input array.
     *
     * @param array               the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue (&gt;array.length)
     *                            results in no
     *                            change.
     * @param endIndexExclusive   elements up to endIndex-1 are reversed in the array. Undervalue (&lt; start index)
     *                            results in no
     *                            change. Overvalue (&gt;array.length) is demoted to array length.
     * @since 3.2
     */
    public static void reverse(final long[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        long tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * <p>Reverses the order of the given array.
     *
     * <p>This method does nothing for a {@code null} input array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final int[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * <p>
     * Reverses the order of the given array in the given range.
     *
     * <p>
     * This method does nothing for a {@code null} input array.
     *
     * @param array               the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue (&gt;array.length)
     *                            results in no
     *                            change.
     * @param endIndexExclusive   elements up to endIndex-1 are reversed in the array. Undervalue (&lt; start index)
     *                            results in no
     *                            change. Overvalue (&gt;array.length) is demoted to array length.
     * @since 3.2
     */
    public static void reverse(final int[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        int tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * <p>Reverses the order of the given array.
     *
     * <p>This method does nothing for a {@code null} input array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final short[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * <p>
     * Reverses the order of the given array in the given range.
     *
     * <p>
     * This method does nothing for a {@code null} input array.
     *
     * @param array               the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue (&gt;array.length)
     *                            results in no
     *                            change.
     * @param endIndexExclusive   elements up to endIndex-1 are reversed in the array. Undervalue (&lt; start index)
     *                            results in no
     *                            change. Overvalue (&gt;array.length) is demoted to array length.
     * @since 3.2
     */
    public static void reverse(final short[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        short tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * <p>Reverses the order of the given array.
     *
     * <p>This method does nothing for a {@code null} input array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final char[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * <p>
     * Reverses the order of the given array in the given range.
     *
     * <p>
     * This method does nothing for a {@code null} input array.
     *
     * @param array               the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue (&gt;array.length)
     *                            results in no
     *                            change.
     * @param endIndexExclusive   elements up to endIndex-1 are reversed in the array. Undervalue (&lt; start index)
     *                            results in no
     *                            change. Overvalue (&gt;array.length) is demoted to array length.
     * @since 3.2
     */
    public static void reverse(final char[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        char tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * <p>Reverses the order of the given array.
     *
     * <p>This method does nothing for a {@code null} input array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final byte[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * <p>
     * Reverses the order of the given array in the given range.
     *
     * <p>
     * This method does nothing for a {@code null} input array.
     *
     * @param array               the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue (&gt;array.length)
     *                            results in no
     *                            change.
     * @param endIndexExclusive   elements up to endIndex-1 are reversed in the array. Undervalue (&lt; start index)
     *                            results in no
     *                            change. Overvalue (&gt;array.length) is demoted to array length.
     * @since 3.2
     */
    public static void reverse(final byte[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * <p>Reverses the order of the given array.
     *
     * <p>This method does nothing for a {@code null} input array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final double[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * <p>
     * Reverses the order of the given array in the given range.
     *
     * <p>
     * This method does nothing for a {@code null} input array.
     *
     * @param array               the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue (&gt;array.length)
     *                            results in no
     *                            change.
     * @param endIndexExclusive   elements up to endIndex-1 are reversed in the array. Undervalue (&lt; start index)
     *                            results in no
     *                            change. Overvalue (&gt;array.length) is demoted to array length.
     * @since 3.2
     */
    public static void reverse(final double[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        double tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * <p>Reverses the order of the given array.
     *
     * <p>This method does nothing for a {@code null} input array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final float[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * <p>
     * Reverses the order of the given array in the given range.
     *
     * <p>
     * This method does nothing for a {@code null} input array.
     *
     * @param array               the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue (&gt;array.length)
     *                            results in no
     *                            change.
     * @param endIndexExclusive   elements up to endIndex-1 are reversed in the array. Undervalue (&lt; start index)
     *                            results in no
     *                            change. Overvalue (&gt;array.length) is demoted to array length.
     * @since 3.2
     */
    public static void reverse(final float[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        float tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * <p>Reverses the order of the given array.
     *
     * <p>This method does nothing for a {@code null} input array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final boolean[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * <p>
     * Reverses the order of the given array in the given range.
     *
     * <p>
     * This method does nothing for a {@code null} input array.
     *
     * @param array               the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue (&gt;array.length)
     *                            results in no
     *                            change.
     * @param endIndexExclusive   elements up to endIndex-1 are reversed in the array. Undervalue (&lt; start index)
     *                            results in no
     *                            change. Overvalue (&gt;array.length) is demoted to array length.
     * @since 3.2
     */
    public static void reverse(final boolean[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        boolean tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

// -----------------------------------------------------------------------------------------------
// ------------------------------------------------leaderli---------------------------------------
// -----------------------------------------------------------------------------------------------


    /**
     * 合并两个数组，并返回合并后的新数组。
     *
     * @param firstArray  第一个数组
     * @param secondArray 第二个数组
     * @param <T>         数组元素的类型
     * @return 合并后的新数组
     * @throws IllegalArgumentException 如果参数不是数组，或者两个参数都为null
     */
    @SuppressWarnings({"unchecked", "java:S2259"})
    public static <T> T combineArrays(T firstArray, T secondArray) {

        String thereIsNoArray = "there is no array";
        if (firstArray == null) {
            LiAssertUtil.assertTrue(isArray(secondArray), IllegalArgumentException::new, thereIsNoArray);
            return arraycopy(secondArray);
        }

        if (secondArray == null) {
            LiAssertUtil.assertTrue(isArray(firstArray), IllegalArgumentException::new, thereIsNoArray);
            return arraycopy(firstArray);

        }
        Class<?> firstArrayComponentType = firstArray.getClass().getComponentType();
        Class<?> secondArrayComponentType = secondArray.getClass().getComponentType();

        LiAssertUtil.assertTrue(isArray(firstArray) && isArray(secondArray), IllegalArgumentException::new, thereIsNoArray);
        LiAssertUtil.assertTrue(secondArrayComponentType == firstArrayComponentType, IllegalArgumentException::new, "two array don't have same componentType");


        int firstArrayLength = getLength(firstArray);
        int secondArrayLength = getLength(secondArray);

        T combinedArray = (T) Array.newInstance(ClassUtil.getComponentType(firstArray), firstArrayLength + secondArrayLength);
        System.arraycopy(firstArray, 0, combinedArray, 0, firstArrayLength);
        System.arraycopy(secondArray, 0, combinedArray, firstArrayLength, secondArrayLength);

        return combinedArray;
    }

    /**
     * 判断一个对象是否为数组
     *
     * @param obj 待判断的对象
     * @return 如果对象是数组则返回true，否则返回false
     */
    public static boolean isArray(Object obj) {

        if (obj == null) {
            return false;
        }
        return obj.getClass().isArray();

    }

    /**
     * 复制一个新的数组
     *
     * @param array 原始数组
     * @param <T>   数组类型
     * @return 返回一个新的数组
     * @throws IllegalArgumentException 如果传入的参数不是数组
     * @throws NullPointerException     如果传入的参数为null
     */

    @SuppressWarnings("unchecked")
    public static <T> T arraycopy(T array) {


        LiAssertUtil.assertTrue(isArray(array), IllegalArgumentException::new, "it's not a array");
        int length = getLength(array);
        T target = (T) Array.newInstance(array.getClass().getComponentType(), length);
        System.arraycopy(array, 0, target, 0, length);
        return target;
    }

    /**
     * 返回一个原始数组的子数组。子数组从指定的beginIndex开始，一直延伸到index为endIndex-1的元素。
     * 子数组的长度为endIndex-beginIndex。
     *
     * @param origin     原始数组
     * @param beginIndex 开始索引，支持负数，表示从末尾计算的位置
     * @param endIndex   结束索引，支持负数，表示从末尾计算的位置；支持0，表示endIndex=origin.length
     * @param <T>        数组类型
     * @return 新的子数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T subArray(T origin, int beginIndex, int endIndex) {
        Class<?> componentType = ClassUtil.getComponentType(origin);
        if (componentType == null) {
            return null;
        }
        int length = Array.getLength(origin);
        beginIndex = calculateBeginIndex(beginIndex, length);
        endIndex = calculateEndIndex(endIndex, length);
        T sub;
        if (beginIndex < 0 || beginIndex >= length || endIndex <= beginIndex) {
            sub = (T) Array.newInstance(componentType, 0);
        } else {
            sub = (T) Array.newInstance(componentType, endIndex - beginIndex);
            System.arraycopy(origin, beginIndex, sub, 0, endIndex - beginIndex);
        }
        return sub;
    }


    /**
     * 返回一个非负的起始索引。如果索引超出边界，则使用边界值
     *
     * @param startIndex 起始索引，可能是负数
     * @param arrLength  数组的长度
     * @return 非负的起始索引
     */
    public static int calculateBeginIndex(int startIndex, int arrLength) {
        if (arrLength < 1) {
            return 0;
        }
        if (startIndex < 0) {
            startIndex += arrLength;

            if (startIndex < 0) {
                return 0;
            }
        }
        return startIndex;
    }

    /**
     * 返回一个非负的结束索引。如果索引超出边界，则使用边界值
     *
     * @param endIndex  结束索引，可能是负数
     * @param arrLength 数组的长度
     * @return 非负的结束索引
     */
    private static int calculateEndIndex(int endIndex, int arrLength) {
        if (arrLength <= 0) {
            return 0;
        }
        if (endIndex > arrLength) {
            return arrLength;
        }
        if (endIndex < 1) {
            endIndex += arrLength;

            if (endIndex < 0) {
                return 0;
            }
        }
        return endIndex;
    }


    /**
     * 从原始数组中移除一个子集，返回一个新的数组。移除的子集从指定的 {@code startIndex} 开始，
     * <p>
     * 并扩展到索引 {@code endIndex - 1} 的元素。因此，新的子集的长度为 {@code array.length - (endIndex - startIndex)}。
     *
     * @param array      原始数组
     * @param startIndex 移除子集的开始索引。支持负数，表示从后面计算的位置
     * @param endIndex   移除子集的结束索引。支持负数，表示从后面计算的位置。支持零，表示 {@code endIndex = array.length}
     * @param <T>        数组类的类型
     * @return 移除子集后的新数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T removeSub(T array, int startIndex, int endIndex) {

        Class<?> componentType = ClassUtil.getComponentType(array);
        LiAssertUtil.assertNotNull(componentType, IllegalArgumentException::new, "it's not array or it's null");

        int length = getLength(array);

        endIndex = calculateEndIndex(endIndex, length);
        startIndex = calculateBeginIndex(startIndex, length);

        T newArray;
        if (startIndex < 0 || endIndex <= startIndex) {
            newArray = array;
        } else if (startIndex >= length) {
            newArray = (T) Array.newInstance(componentType, 0);
        } else {
            newArray = (T) Array.newInstance(componentType, length - (endIndex - startIndex));

            System.arraycopy(array, 0, newArray, 0, startIndex);
            System.arraycopy(array, endIndex, newArray, startIndex, length - endIndex);
        }
        return newArray;
    }

    /**
     * 将一个数组添加到另一个数组的末尾并返回新的数组。
     *
     * @param original 原数组
     * @param toAdd    要添加的数组
     * @param <T>      元素类型
     * @return 添加后的新数组
     */
    @SafeVarargs
    public static <T> T[] append(T[] original, T... toAdd) {
        return insert(original, Integer.MAX_VALUE, toAdd);
    }

    /**
     * 在指定位置插入数组元素并返回新的数组。
     *
     * @param original   原数组
     * @param beginIndex 插入起始位置 支持负数，表示从后面计算的位置
     * @param inserts    要插入的元素 支持负数，表示从后面计算的位置。支持零，表示 {@code endIndex = array.length}
     * @param <T>        元素类型
     * @return 插入后的新数组
     * @see #calculateBeginIndex(int, int)
     * @see #calculateEndIndex(int, int)
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <T> T[] insert(T[] original, int beginIndex, T... inserts) {

        Class<?> componentType = ClassUtil.getComponentType(original);
        if (componentType == null) {
            return arraycopy(inserts);
        }
        if (inserts == null || inserts.length == 0) {
            return arraycopy(original);
        }
        int length = original.length;
        T[] arr = (T[]) ClassUtil.newWrapperArray(componentType, length + inserts.length);

        beginIndex = calculateBeginIndex(beginIndex, length);
        if (beginIndex >= length) {
            System.arraycopy(original, 0, arr, 0, length);
            System.arraycopy(inserts, 0, arr, length, inserts.length);
        } else {
            System.arraycopy(original, 0, arr, 0, beginIndex);
            System.arraycopy(inserts, 0, arr, beginIndex, inserts.length);
            System.arraycopy(original, beginIndex, arr, beginIndex + inserts.length, length - beginIndex);
        }


        return arr;
    }


    /**
     * 将可变类型参数转换为数组
     *
     * @param elements 可变参数
     * @param <T>      数组的类型
     * @return 一个新的数组，如果参数是基本类型则返回其包装类型的数组
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <T> T[] of(T... elements) {
        Class<?> componentType = elements.getClass().getComponentType();
        if (componentType.isPrimitive()) {
            T[] ts = (T[]) ClassUtil.newWrapperArray(componentType, elements.length);
            System.arraycopy(elements, 0, ts, 0, ts.length);
        }
        return elements;
    }

    /**
     * 将给定对象转换为字符串表示形式。
     *
     * @param obj 要转换的对象
     * @return 对象的字符串表示形式
     */
    public static String toString(Object obj) {

        if (isArray(obj)) {

            Object[] arr = toArray(obj);


            StringBuilder sb = new StringBuilder("[");
            //noinspection ConstantConditions
            for (int i = 0; i < arr.length; i++) {

                String element = toString(arr[i]);

                if (i == arr.length - 1) {
                    sb.append(element);
                } else {
                    sb.append(element).append(", ");
                }
            }
            return sb.append("]").toString();
        }
        return String.valueOf(obj);

    }

    /**
     * 将可变参数转换为指定类型的数组
     *
     * @param componentType 数组的类型
     * @param elements      源数组
     * @param <T>           数组的类型
     * @return 新数组，其类型为T
     * @throws ArrayStoreException 如果数组中包含其他类型的成员
     */
    @SafeVarargs
    public static <T> T[] toWrapperArray(Class<T> componentType, T... elements) {
        T[] arr = ClassUtil.newWrapperArray(componentType, elements.length);
        System.arraycopy(elements, 0, arr, 0, elements.length);
        return arr;
    }

    public static int[] toIntArray(Integer... elements) {
        int[] arr = (int[]) Array.newInstance(int.class, elements.length);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = elements[i];
        }
        return arr;
    }


    /**
     * 将 {@link  Iterable} 可提供的元素转换为类型为{@code T}的数组。
     *
     * @param <T>           可迭代对象提供的元素类型
     * @param componentType 数组的类型
     * @param iterable      迭代对象
     * @return 类型为{@code T}的数组，其中包含{@link  Iterator} 提供的元素
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Class<T> componentType, Iterable<T> iterable) {
        if (iterable != null) {

            List<T> list = new ArrayList<>();
            iterable.forEach(list::add);
            T[] array = (T[]) Array.newInstance(componentType, list.size());
            for (int i = 0; i < list.size(); i++) {
                array[i] = list.get(i);
            }

            return array;
        }
        return ClassUtil.newWrapperArray(componentType, 0);

    }


    /**
     * 将 {@link  Stream} 可提供的元素转换为类型为{@code T}的数组。
     *
     * @param <T>           可迭代对象提供的元素类型
     * @param componentType 数组的类型
     * @param stream        流对象
     * @return 类型为{@code T}的数组，其中包含{@link  Stream} 提供的元素
     */
    public static <T> T[] toArray(Class<T> componentType, Stream<T> stream) {
        if (stream != null) {
            Object[] array = stream.toArray();
            T[] result = ClassUtil.newWrapperArray(componentType, array.length);
            System.arraycopy(array, 0, result, 0, array.length);
            return result;
        }
        return ClassUtil.newWrapperArray(componentType, 0);

    }

    /**
     * 将 {@link  Iterator} 可提供的元素转换为类型为{@code T}的数组。
     *
     * @param <T>           可迭代对象提供的元素类型
     * @param componentType 数组的类型
     * @param iterator      迭代
     * @return 类型为{@code T}的数组，其中包含{@link  Iterator} 提供的元素
     */
    public static <T> T[] toArray(Class<T> componentType, Iterator<T> iterator) {
        if (iterator != null) {

            List<T> list = new ArrayList<>();
            iterator.forEachRemaining(list::add);
            return CollectionUtils.toArray(componentType, list);
        }
        return ClassUtil.newWrapperArray(componentType, 0);
    }

    /**
     * 将 {@link  Enumeration} 可提供的元素转换为类型为{@code T}的数组。
     *
     * @param <T>           可迭代对象提供的元素类型
     * @param componentType 数组的类型
     * @param enumeration   迭代
     * @return 类型为{@code T}的数组，其中包含{@link  Enumeration} 提供的元素
     */

    public static <T> T[] toArray(Class<T> componentType, Enumeration<T> enumeration) {

        if (enumeration != null) {
            List<T> list = new ArrayList<>();

            while (enumeration.hasMoreElements()) {
                list.add(enumeration.nextElement());
            }

            return CollectionUtils.toArray(componentType, list);
        }
        return ClassUtil.newWrapperArray(componentType, 0);

    }

    /**
     * 将 Object 对象转换为数组，如果 obj 不是数组则返回 {@code null}
     *
     * <p>
     * 如果数组的元素类型为基本数据类型，则将其转换为对应的包装类型数组
     *
     * @param <T> 数组的类型
     * @param obj 声明为 Object 类型的数组对象
     * @return 转换后的数组对象
     */

    @SuppressWarnings({"unchecked", "java:S1168"})
    public static <T> T[] toArray(Object obj) {

        Class<?> elementType = ClassUtil.getComponentType(obj);
        if (elementType == null) {
            return null;
        }
        int length = Array.getLength(obj);
        T[] arr = (T[]) ClassUtil.newWrapperArray(elementType, length);

        for (int i = 0; i < length; i++) {
            arr[i] = (T) Array.get(obj, i);
        }

        return arr;
    }


    /**
     * @param origin         源数组
     * @param componentClass 目标数组类型
     * @param mapper         函数转换类
     * @param <T>            源数组类型泛型
     * @param <R>            目标数组类型泛型
     * @return 转换后的目标数组
     */
    public static <T, R> R[] map(T[] origin, Class<R> componentClass, Function<? super T, ? extends R> mapper) {
        if (origin == null) {
            return ClassUtil.newWrapperArray(componentClass, 0);
        }
        R[] target = ClassUtil.newWrapperArray(componentClass, origin.length);

        for (int i = 0; i < origin.length; i++) {
            target[i] = mapper.apply(origin[i]);
        }
        return target;
    }

    public static <T, R> R[] flatMap(T[] origin, Class<? extends R> componentClass, Function<? super T, ? extends R[]> mapper) {
        List<R> result = new ArrayList<>();
        if (origin != null) {
            for (T t : origin) {
                result.addAll(Arrays.asList(mapper.apply(t)));
            }
        }
        return result.toArray((R[]) ClassUtil.newWrapperArray(componentClass, 0));
    }
}
