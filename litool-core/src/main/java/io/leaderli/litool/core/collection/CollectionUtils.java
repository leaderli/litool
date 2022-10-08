package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.util.ObjectsUtil;

import java.lang.reflect.Array;
import java.util.*;

/**
 * <p> Operations on collection
 *
 * @author leaderli
 * @since 2022 /7/20
 */
public class CollectionUtils {
    /**
     * Return the element that appears multiple times
     *
     * @param <T>      the type of elements returned by this iterable
     * @param iterable provide elements by {@link  Iterable#iterator()}
     * @return a lira consists the element that appears multiple times
     */
    public static <T> Lira<T> getDuplicateElements(Iterable<? extends T> iterable) {

        if (isEmpty(iterable)) {
            return Lira.none();
        }

        Set<T> duplicate = new HashSet<>();

        Set<T> unique = new HashSet<>();

        for (T t : iterable) {
            if (!unique.add(t)) {
                duplicate.add(t);
            }
        }

        return Lira.of(duplicate);
    }

    /**
     * Return an iterable has element
     *
     * @param iterable an iterable
     * @return an iterable has element
     */
    public static boolean isEmpty(Iterable<?> iterable) {
        if (iterable == null) {
            return true;
        }
        Iterator<?> iterator = iterable.iterator();
        return !iterator.hasNext();
    }

    /**
     * Return a new empty ArrayList
     *
     * @param <T> the type of elements in this list
     * @return a new empty ArrayList
     */
    public static <T> List<T> emptyList() {

        return new ArrayList<>();
    }

    /**
     * Of list.
     *
     * @param <T>      the type parameter
     * @param elements the elements
     * @return the list
     */
    @SafeVarargs
    public static <T> List<T> of(T... elements) {

        List<T> arrayList = new ArrayList<>();
        if (elements != null) {
            Collections.addAll(arrayList, elements);
        }
        return arrayList;
    }

    /**
     * Return Cartesian Product, return {new Object[0][]} if
     * any element is null or empty array
     *
     * @param elements the elements of cartesian
     * @return return cartesian of elements
     */
    @SuppressWarnings("ConstantConditions")
    public static Object[][] cartesian(Object[]... elements) {
        if (elements == null || elements.length == 0) {
            return new Object[0][0];
        }

        Object[] head = elements[0];
        if (head == null) {
            return new Object[0][];
        }
        head = Arrays.stream(head).distinct().toArray();


        Object[][] result = new Object[head.length][];

        for (int i = 0; i < result.length; i++) {
            result[i] = new Object[]{head[i]};
        }


        while ((elements = ArrayUtils.subArray(elements, 1, 0)).length > 0) {
            if (result.length == 0) {
                return new Object[0][];
            }
            Object[] right = Lira.of(elements[0]).distinct().toArray();
            Object[][] temps = new Object[result.length * right.length][];

            int i = 0;
            for (Object[] left : result) {

                for (Object r : right) {
                    temps[i++] = ArrayUtils.add(left, r);
                }
            }
            result = temps;

        }
        return result;
    }


    /**
     * return {@code  null} if obj is not array otherwise cast to array
     * <p>
     * if array's element is primitive, will convert to  wrapper array
     *
     * @param <T> the type of array
     * @param obj the array that declare as Object
     * @return casted array
     */

    @SuppressWarnings({"unchecked", "java:S1168"})
    public static <T> T[] toArray(Object obj) {

        Class<?> componentType = ClassUtil.getComponentType(obj);
        if (componentType == null) {
            return null;
        }
        int length = Array.getLength(obj);
        T[] objects = (T[]) ClassUtil.newWrapperArray(componentType, length);

        for (int i = 0; i < length; i++) {
            objects[i] = (T) Array.get(obj, i);
        }

        return objects;
    }

    /**
     * Return the xor of two lira
     *
     * @param <T> the  type of lira element
     * @param a   a lira
     * @param b   another lira
     * @return xor of two lira
     */
    public static <T> Lira<T> xor(Iterable<T> a, Iterable<T> b) {

        Lira<T> union = union(a, b);
        List<T> intersection = intersection(a, b).get();
        return union.filter(e -> !intersection.contains(e));
    }

    /**
     * Return the union of two array
     *
     * @param <T> the type of array
     * @param a   a array
     * @param b   another array
     * @return the union of two array
     * @see #union(Iterable, Iterable)
     */
    public static <T> Lira<T> union(Iterable<T> a, Iterable<T> b) {


        Lira<T> left = Lira.of(a).distinct();
        if (b == null) {
            return left;
        }
        List<T> raw = left.get();

        b.forEach(raw::add);

        return Lira.of(raw).distinct();

    }

    /**
     * @param t1   arr 1
     * @param t2   arr 2
     * @param <T1> the type of t1
     * @param <T2> the type of t2
     * @return combine two same length arr  as a lira of {@link LiTuple2}
     */
    public static <T1, T2> Lira<LiTuple2<T1, T2>> tuple(T1[] t1, T2[] t2) {

        ObjectsUtil.requireNotNull(t1, t2);
        LiAssertUtil.assertTrue(t1.length == t2.length);

        List<LiTuple2<T1, T2>> list = new ArrayList<>();
        for (int i = 0; i < t1.length; i++) {

            list.add(LiTuple.of(t1[i], t2[i]));
        }
        return Lira.of(list);

    }

    /**
     * Return intersection of two lira
     *
     * @param <T> the type of lira
     * @param a   a lira
     * @param b   another lira
     * @return intersection of two lira
     */
    public static <T> Lira<T> intersection(Iterable<T> a, Iterable<T> b) {


        List<T> result = new ArrayList<>();


        List<T> raw = Lira.of(b).get();


        if (a == null) {
            return Lira.none();
        }
        a.forEach(t -> {

            if (raw.contains(t)) {
                result.add(t);
            }
        });

        return Lira.of(result).distinct();


    }

    /**
     * Return the union of two array
     *
     * @param <T> the type of array
     * @param a   a array
     * @param b   another array
     * @return the union of two array
     * @see #union(Iterable, Iterable) #union(Iterable, Iterable)
     */
    public static <T> Lira<T> union(T[] a, T[] b) {

        return union(Lira.of(a), Lira.of(b));
    }


}
