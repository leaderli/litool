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
 * <p>  操作集合的工具类
 *
 * @author leaderli
 * @since 2022 /7/20
 */
public class CollectionUtils {


    /**
     * 返回迭代对象中出现多次的元素
     *
     * @param <T>      迭代对象中元素的类型
     * @param iterable 通过{@link Iterable#iterator()}提供的元素
     * @return 包含在迭代对象中出现多次的元素的 {@link  Lira}
     */

    public static <T> Lira<T> getDuplicateElements(Iterable<? extends T> iterable) {

        Set<T> unique = new HashSet<>();
        return Lira.<T>of(iterable).filter(e -> !unique.add(e)).unique();
    }


    /**
     * 返回一个新的空 {@link ArrayList}
     *
     * @param <T> 列表中元素的类型
     * @return 一个新的空 {@link ArrayList}
     */
    public static <T> ArrayList<T> emptyList() {

        return new ArrayList<>();
    }

    /**
     * 将指定元素转换为 {@link ArrayList}
     *
     * @param <T>      列表中元素的类型
     * @param elements 待转换的元素
     * @return 转换后的 {@link ArrayList}
     */
    @SafeVarargs
    public static <T> ArrayList<T> toList(T... elements) {

        ArrayList<T> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, elements);
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
            Object[] right = Lira.of(elements[0]).distinct().toNullableArray(Object.class);
            Object[][] temps = new Object[result.length * right.length][];

            int i = 0;
            for (Object[] left : result) {

                for (Object r : right) {
                    temps[i++] = ArrayUtils.append(left, r);
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
     * @param a   a  itr
     * @param b   another itr
     * @return the union of two  itr
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
     * Return intersection of two arr
     *
     * @param <T> the type of  array
     * @param a   a arr
     * @param b   another arr
     * @return intersection of two arr
     */
    public static <T> Lira<T> intersection(T[] a, T[] b) {

        return intersection(Lira.of(a), Lira.of(b));
    }

    /**
     * Return intersection of two itr
     *
     * @param <T> the type of itr
     * @param a   a itr
     * @param b   another itr
     * @return intersection of two itr
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
