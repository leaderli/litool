package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassUtil;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

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
    public static List<List<Object>> cartesianProduct(List<Object>... elements) {

        if (elements == null || elements.length == 0) {
            return emptyList();
        }
        List<List<Object>> x = Lira.of(elements[0]).map(Arrays::asList).getRaw();

        for (int i = 1; i < elements.length; i++) {

            List<Object> y = elements[i];

            x = x.stream()
                    .map(li ->


                            Lira.of(y)
                                    .map(e -> {
                                        List<Object> raw = Lira.of(li).getRaw();
                                        raw.add(e);
                                        return raw;
                                    })
                                    .getRaw()
                    )
                    .flatMap(List::stream).collect(Collectors.toList());
        }

        return x;
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
     * @return 两个集合的并集
     */
    public static <T> Lira<T> union(Lira<T> a, Lira<T> b) {


        List<T> raw = a.getRaw();

        b.forEach(raw::add);

        return Lira.of(raw).distinct();

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
}
