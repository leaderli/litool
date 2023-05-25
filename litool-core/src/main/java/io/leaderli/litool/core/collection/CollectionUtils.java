package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.util.ObjectsUtil;

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
        return Lira.<T>of(iterable).filter(e -> !unique.add(e)).distinct();
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
     * 返回元素的笛卡尔积，如果任何元素为null或空数组，则返回{new Object[0][]}
     *
     * @param elements 笛卡尔积的元素
     * @return 返回元素的笛卡尔积
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
     * @param <T>    元素类型
     * @param first  -
     * @param second -
     * @return 两个 {@link  Iterable}的异或合集
     */
    public static <T> Lira<T> xor(Class<T> type, Iterable<T> first, Iterable<T> second) {

        Lira<T> union = union(type, first, second);
        List<T> intersection = intersection(type, first, second).get();
        return union.filter(e -> !intersection.contains(e));
    }


    /**
     * @param <T>    元素类型
     * @param first  -
     * @param second -
     * @return 两个 {@link  Iterable}的合集，union 需要保证顺序
     * @see #union(Class, Object[], Object[])
     */
    public static <T> Lira<T> union(Class<T> type, Iterable<T> first, Iterable<T> second) {

        return union(type, ArrayUtils.toArray(type, first), ArrayUtils.toArray(type, second));

    }

    /**
     * @param <T>    元素类型
     * @param first  -
     * @param second -
     * @return 两个 {@link  Iterable}的合集，union 需要保证顺序
     */
    public static <T> Lira<T> union(Class<T> type, T[] first, T[] second) {


        return Lira.of(first)
                .terminalMap(prev -> {
                    LinkedHashSet<T> orderSet = new LinkedHashSet<>(prev);
                    Lira.of(second).forEach(orderSet::add);
                    return orderSet;
                });
    }

    /**
     * 将两个长度相同的数组合并成一个由{@link LiTuple2}组成的列表
     *
     * @param arr1 第一个数组
     * @param arr2 第二个数组
     * @param <T1> arr1的元素类型
     * @param <T2> arr2的元素类型
     * @return 合并后的列表，其中每个元素都是一个由t1和t2组成的{@link LiTuple2}
     * @throws IllegalArgumentException 当arr1和arr2的长度不一致时抛出
     */
    public static <T1, T2> Lira<LiTuple2<T1, T2>> tuple(T1[] arr1, T2[] arr2) {

        ObjectsUtil.requireNotNull(arr1, arr2);
        LiAssertUtil.assertTrue(arr1.length == arr2.length, IllegalArgumentException::new, "arr length not same");

        List<LiTuple2<T1, T2>> list = new ArrayList<>();
        for (int i = 0; i < arr1.length; i++) {

            list.add(LiTuple.of(arr1[i], arr2[i]));
        }
        return Lira.of(list);

    }

    /**
     * 返回两个数组的交集
     *
     * @param <T>    数组的元素类型
     * @param type   数组的元素类型的Class对象
     * @param first  第一个数组
     * @param second 第二个数组
     * @return 两个数组的交集
     */
    public static <T> Lira<T> intersection(Class<T> type, T[] first, T[] second) {


        if (first == null || first.length == 0 || second == null || second.length == 0) {
            return Lira.none();
        }


        List<T> result = new ArrayList<>();

        for (T f : first) {

            for (T s : second) {

                if (Objects.equals(f, s)) {
                    result.add(f);
                    break;
                }
            }
        }

        return Lira.of(result).distinct();

    }

    /**
     * 返回两个迭代器的交集
     *
     * @param <T>    数组的元素类型
     * @param type   数组的元素类型的Class对象
     * @param first  第一个迭代器
     * @param second 第二个迭代器
     * @return {@link #intersection(Class, Object[], Object[])}
     */
    public static <T> Lira<T> intersection(Class<T> type, Iterable<T> first, Iterable<T> second) {

        return intersection(type, ArrayUtils.toArray(type, first), ArrayUtils.toArray(type, second));


    }


}
