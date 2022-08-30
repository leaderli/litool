/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.exception.AssertException;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.PrimitiveEnum;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
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
     * Returns  the combination of  two array.
     * <p>
     *
     * @param a   an array
     * @param b   an array
     * @param <T> the type of  array
     * @return an new combined array
     * @throws AssertException when param is not array or both param is null
     */
    @SuppressWarnings({"unchecked", "java:S2259"})
    public static <T> T combination(T a, T b) {

        LiAssertUtil.assertTrue(isArray(a) || isArray(b), "there is no array");

        int a_len;
        int b_len;


        if (a == null || (a_len = Array.getLength(a)) == 0) {
            return arraycopy(b);
        }
        if (b == null || (b_len = Array.getLength(b)) == 0) {
            return arraycopy(a);
        }

        T union = (T) Array.newInstance(ClassUtil.getComponentType(a), a_len + b_len);
        System.arraycopy(a, 0, union, 0, a_len);
        System.arraycopy(b, 0, union, a_len, b_len);

        return union;
    }

    /**
     * Return whether a object is array
     *
     * @param arr a object
     * @return whether a object is array
     */
    public static boolean isArray(Object arr) {

        if (arr == null) {
            return false;
        }
        return arr.getClass().isArray();

    }

    /**
     * @param origin an array
     * @param <T>    the type of array
     * @return clone a new array
     * @throws IllegalArgumentException if the object argument is not arr
     * @throws NullPointerException     if the object argument is null
     */

    @SuppressWarnings("unchecked")
    public static <T> T arraycopy(T origin) {

        int length = Array.getLength(origin);
        T target = (T) Array.newInstance(origin.getClass().getComponentType(), length);
        System.arraycopy(origin, 0, target, 0, length);
        return target;
    }

    /**
     * Returns a array that is a sub of  origin array. The sub begins at the
     * specified {@code  beginIndex} and extends to the element at index {@code endIndex - 1}.
     * Thus the length of the sub is {@code  endIndex - beginIndex}.
     *
     * @param origin     an array
     * @param beginIndex the beginning index, Negative numbers are supported, indicating
     *                   the position calculated from the back
     *                   Negative numbers are supported, indicating the position calculated from the back
     * @param endIndex   the ending index, Negative number are supported, indicating
     *                   the position calculated from the back. zero number are supported,
     *                   indicating {@code endIndex = origin.length}
     * @param <T>        the type of array class
     * @return new sub array
     */
    @SuppressWarnings("unchecked")
    public static <T> T subArray(T origin, int beginIndex, int endIndex) {

        Class<T> componentType = ClassUtil.getComponentType(origin);

        if (componentType == null) {
            return null;
        }

        int length = Array.getLength(origin);
        beginIndex = correctBeginIndex(beginIndex, length);
        endIndex = correctEndIndex(endIndex, length);

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
     * Return a non-negative end index. If index exceeds bounds, use bounds value
     *
     * @param beginIndex the begin index may be negative
     * @param arr_length the length of arr
     * @return a non-negative begin index
     */
    private static int correctBeginIndex(int beginIndex, int arr_length) {
        if (arr_length < 1) {
            return 0;
        }
        if (beginIndex < 0) {
            beginIndex += arr_length;

            if (beginIndex < 0) {
                return 0;
            }
        }
        return beginIndex;
    }

    /**
     * Return a non-negative end index. If index exceeds bounds, use bounds value
     *
     * @param endIndex   the end index may be negative
     * @param arr_length the length of arr
     * @return a non-negative end index
     */
    private static int correctEndIndex(int endIndex, int arr_length) {
        if (arr_length <= 0) {
            return 0;
        }
        if (endIndex > arr_length) {
            return arr_length;
        }
        if (endIndex < 1) {
            endIndex += arr_length;

            if (endIndex < 0) {
                return 0;
            }
        }
        return endIndex;
    }

    /**
     * Returns a array that is a removed of  origin array. The remove begins at the
     * specified {@code  beginIndex} and extends to the element at index {@code endIndex - 1}.
     * Thus the length of the sub is {@code  origin.length - (endIndex - beginIndex)}.
     *
     * @param origin     an array
     * @param beginIndex the beginning index, Negative numbers are supported, indicating
     *                   the position calculated from the back
     *                   Negative numbers are supported, indicating the position calculated from the back
     * @param endIndex   the ending index, Negative number are supported, indicating
     *                   the position calculated from the back. zero number are supported,
     *                   indicating {@code endIndex = origin.length}
     * @param <T>        the type of array class
     * @return new removed array
     */
    @SuppressWarnings("unchecked")
    public static <T> T remove(T origin, int beginIndex, int endIndex) {

        Class<T> componentType = ClassUtil.getComponentType(origin);

        if (componentType == null) {
            return null;
        }

        int length = Array.getLength(origin);
        endIndex = correctEndIndex(endIndex, length);
        beginIndex = correctBeginIndex(beginIndex, length);

        T removed;
        if (beginIndex < 0 || endIndex <= beginIndex) {
            removed = origin;
        } else if (beginIndex >= length) {
            removed = (T) Array.newInstance(componentType, 0);
        } else {
            removed = (T) Array.newInstance(componentType, length - (endIndex - beginIndex));

            System.arraycopy(origin, 0, removed, 0, beginIndex);
            System.arraycopy(origin, endIndex, removed, beginIndex, length - endIndex);
        }
        return removed;
    }

    /**
     * Returns a array that append a array after origin array.
     *
     * @param origin an array
     * @param <T>    the type of elements
     * @return new appended array
     */
    @SafeVarargs
    public static <T> T[] add(T[] origin, T... add) {
        return insert(origin, Integer.MAX_VALUE, add);
    }

    /**
     * Returns a array that insert a array into origin array.
     * The insert begins at the specified {@code  beginIndex}
     *
     * @param origin     an array
     * @param beginIndex the beginning index, Negative numbers are supported, indicating
     *                   the position calculated from the back
     * @param <T>        the type of elements
     * @return new inserted array
     */

    @SafeVarargs
    public static <T> T[] insert(T[] origin, int beginIndex, T... add) {

        Class<T> componentType = ClassUtil.getComponentType(origin);
        if (componentType == null) {
            return arraycopy(add);
        }
        if (add == null || add.length == 0) {
            return arraycopy(origin);
        }
        int length = origin.length;
        T[] arr = ClassUtil.newWrapperArray(componentType, length + add.length);

        beginIndex = correctBeginIndex(beginIndex, length);
        if (beginIndex >= length) {
            System.arraycopy(origin, 0, arr, 0, length);
            System.arraycopy(add, 0, arr, length, add.length);
        } else {
            System.arraycopy(origin, 0, arr, 0, beginIndex);
            System.arraycopy(add, 0, arr, beginIndex, add.length);
            System.arraycopy(origin, beginIndex, arr, beginIndex + add.length, length - beginIndex);
        }


        return arr;
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <T> T[] of(T... arr) {
        Class<?> componentType = arr.getClass().getComponentType();
        if (componentType.isPrimitive()) {
            T[] ts = (T[]) ClassUtil.newWrapperArray(componentType, arr.length);
            System.arraycopy(arr, 0, ts, 0, ts.length);
        }
        return arr;
    }

    /**
     * Return string value of elements
     *
     * @param obj a object
     * @return string value of elements
     */
    public static String toString(Object obj) {

        if (isArray(obj)) {

            Object[] arr = PrimitiveEnum.toWrapperArray(obj);


            StringBuilder sb = new StringBuilder("[");
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
     * Returns an array over the elements that iterable provide of type {@code T}.
     *
     * @param iterable an iterable
     * @param <T>      the type of elements that iterable provide
     * @return Returns an array over the elements that iterable provide of type {@code T}.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        iterable.forEach(list::add);
        return (T[]) list.toArray();
    }

    /**
     * Returns an array over the elements that stream provide of type {@code T}.
     *
     * @param stream an stream
     * @param <T>    the type of elements that stream provide
     * @return Returns an array over the elements that stream provide of type {@code T}.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Stream<T> stream) {
        return (T[]) stream.toArray();
    }

    /**
     * Returns an array over the elements that iterator provide of type {@code T}.
     *
     * @param iterator an iterator
     * @param <T>      the type of elements that iterator provide
     * @return Returns an array over the elements that iterator provide of type {@code T}.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Iterator<T> iterator) {
        List<T> list = new ArrayList<>();
        iterator.forEachRemaining(list::add);

        return (T[]) list.toArray();
    }

    /**
     * Returns an array over the elements that enumeration provide of type {@code T}.
     *
     * @param enumeration an enumeration
     * @param <T>         the type of elements that enumeration provide
     * @return Returns an array over the elements that enumeration provide of type {@code T}.
     */

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Enumeration<T> enumeration) {
        List<T> list = new ArrayList<>();

        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }

        return (T[]) list.toArray();
    }
}
