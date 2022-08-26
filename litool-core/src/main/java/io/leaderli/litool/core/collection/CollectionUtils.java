package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassUtil;

import java.lang.reflect.Array;
import java.util.*;

/**
 * <p> Operations on collection
 *
 * @author leaderli
 * @since 2022/7/20
 */
public class CollectionUtils {
    /**
     * Return an iterable has element
     *
     * @param iterable an iterable
     * @return an iterable has element
     */
    public static boolean isEmpty(Iterable<?> iterable) {
        if (iterable == null) return true;
        Iterator<?> iterator = iterable.iterator();
        return !iterator.hasNext();
    }

    /**
     * Return the element that appears multiple times
     *
     * @param iterable provide elements by {@link  Iterable#iterator()}
     * @param <T>      the type of elements returned by this iterable
     * @return a lira consists the element that appears multiple times
     */
    public static <T> Lira<T> getDuplicateElements(Iterable<? extends T> iterable) {

        if (isEmpty(iterable)) {
            return Lira.none();
        }

        Set<T> duplicate = new HashSet<>();

        Set<T> unique = new HashSet<>();

        for (T t : iterable) {
            System.out.println(t);
            if (!unique.add(t)) {
                duplicate.add(t);
            }
        }

        return Lira.of(duplicate);
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

    @SafeVarargs
    public static <T> List<T> of(T... elements) {

        List<T> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, elements);
        return arrayList;
    }

    /**
     * 笛卡尔积
     *
     * @param elements 元素集合
     * @return 返回多个元素的笛卡尔积
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

        if (head.length == 0) {
            head = new Object[]{null};
        }
        Object[][] result = new Object[head.length][];

        for (int i = 0; i < result.length; i++) {
            result[i] = new Object[]{head[i]};
        }


        while ((elements = ArrayUtils.subArray(elements, 1, 0)).length > 0) {
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
     * @param originalArray 数组
     * @return 如果 originalArray 不是数组 返回 null , 如果 数组是基础类型的数组，则返回其包装类，其他则进行强转
     * return null if  originalArray is null or not arr ;
     * if arr is primitive arr , return the wrapper arr.
     * otherwise cast to arr
     */
    public static Object[] toWrapperArray(Object originalArray) {

        if (originalArray == null || !originalArray.getClass().isArray()) {
            return null;
        }
        Class<?> componentType = originalArray.getClass().getComponentType();
        int length = Array.getLength(originalArray);
        Object[] array = ClassUtil.newWrapperArray(componentType, length);

        for (int i = 0; i < length; i++) {
            array[i] = Array.get(originalArray, i);
        }

        return array;
    }

    /**
     * @param a   集合
     * @param b   集合
     * @param <T> 集合泛型
     * @return 两个集合的异或
     */
    public static <T> Lira<T> xor(Lira<T> a, Lira<T> b) {


        List<T> union = union(a, b).get();
        List<T> intersection = intersection(a, b).get();


        union.removeIf(intersection::contains);

        return Lira.of(union);

    }


    /**
     * @param a   集合
     * @param b   集合
     * @param <T> 集合泛型
     * @return 两个集合的交集
     */
    public static <T> Lira<T> intersection(Lira<T> a, Lira<T> b) {


        List<T> result = new ArrayList<>();

        List<T> raw = b.get();

        a.forEach(t -> {

            if (raw.contains(t)) {
                result.add(t);
            }
        });

        return Lira.of(result).distinct();


    }

    /**
     * @param a   集合
     * @param b   集合
     * @param <T> 集合泛型
     * @return 两个集合的并集
     */
    public static <T> Lira<T> union(T[] a, T[] b) {

        return union(Lira.of(a), Lira.of(b));
    }

    /**
     * @param a   集合
     * @param b   集合
     * @param <T> 集合泛型
     * @return 两个集合的并集
     */
    public static <T> Lira<T> union(Lira<T> a, Lira<T> b) {


        List<T> raw = a.get();

        b.forEach(raw::add);

        return Lira.of(raw).distinct();

    }
}
