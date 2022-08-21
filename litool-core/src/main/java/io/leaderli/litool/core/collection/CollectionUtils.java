package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassUtil;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author leaderli
 * @since 2022/7/20
 */
public class CollectionUtils {
    public static boolean isEmpty(Iterable<?> iterable) {
        if (iterable == null) return false;
        Iterator<?> iterator = iterable.iterator();
        return iterator.hasNext();
    }

    /**
     * @param list 集合
     * @param <T>  集合元素的泛型
     * @return 返回集合中重复的元素集合
     */
    public static <T> List<T> getDuplicateElement(Collection<? extends T> list) {

        if (list == null || list.isEmpty()) {
            return emptyList();
        }

        List<T> duplicate = new ArrayList<>();

        Set<T> unique = new HashSet<>();

        for (T t : list) {
            if (!unique.add(t)) {
                duplicate.add(t);
            }
        }

        return duplicate;

    }

    /**
     * @param <T> 泛型
     * @return 返回一个空的 ArrayList
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


        while ((elements = ArrayUtils.sub(elements, 1, 0)).length > 0) {
            Object[] right = Lira.of(elements[0]).distinct().toArray();
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
        Object[] array = ClassUtil.newArray(componentType, length);

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


        List<T> union = union(a, b).getRaw();
        List<T> intersection = intersection(a, b).getRaw();


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

        List<T> raw = b.getRaw();

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


        List<T> raw = a.getRaw();

        b.forEach(raw::add);

        return Lira.of(raw).distinct();

    }
}
