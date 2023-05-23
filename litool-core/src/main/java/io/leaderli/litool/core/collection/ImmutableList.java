package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.type.ClassUtil;

import java.util.*;
import java.util.stream.Stream;

/**
 * 不可变的列表
 *
 * @param <T> 元素类型
 * @author leaderli
 * @since 2022/8/11
 */
public class ImmutableList<T> implements Iterable<T>, ToList<T, ArrayList<T>>, ToArray<T> {

    private static final ImmutableList<?> NONE_INSTANCE = new NoneImmutableList<>();
    private final int size;
    private final Object[] elements;


    private ImmutableList(Object[] arr) {
        this.size = arr.length;
        this.elements = Arrays.copyOf(arr, size);
    }

    /**
     * 从一个数组中创建一个新的不可变列表
     *
     * @param arr 数组
     * @param <T> 元素类型
     * @return 新的不可变列表
     */
    @SafeVarargs
    public static <T> ImmutableList<T> of(T... arr) {
        if (arr == null || arr.length == 0) {
            return none();
        }
        return new ImmutableList<>(arr);
    }

    /**
     * @param <T> 元素类型
     * @return 一个不包含任何元素的实例
     */

    @SuppressWarnings("unchecked")
    public static <T> ImmutableList<T> none() {
        return (ImmutableList<T>) NONE_INSTANCE;
    }


    /**
     * 将 {@link ArrayItr} 对象转换为 {@link ImmutableList} 对象。
     *
     * @param iterableItr 待转换的 {@link IterableItr} 对象
     * @param <T>         迭代器中元素的类型
     * @return 转换后的 {@link ImmutableList} 对象
     */
    public static <T> ImmutableList<T> of(IterableItr<T> iterableItr) {
        if (iterableItr == NoneItr.of()) {
            return none();
        }
        return new ImmutableList<>(iterableItr.toArray());
    }

    /**
     * 将 {@link Iterator} 对象转换为 {@link ImmutableList} 对象。
     * * @param iterator 待转换的 {@link Iterator} 对象
     *
     * @param iterator -
     * @param <T>      迭代器中元素的类型
     * @return 转换后的 {@link ImmutableList} 对象
     */
    public static <T> ImmutableList<T> of(Iterator<T> iterator) {
        return of(IterableItr.of(iterator));
    }

    /**
     * 从一个可迭代对象中创建一个新的不可变列表
     *
     * @param iterable 可迭代对象
     * @param <T>      元素类型
     * @return 新的不可变列表
     */
    public static <T> ImmutableList<T> of(Iterable<T> iterable) {
        return of(IterableItr.of(iterable));
    }

    /**
     * 将 {@link Enumeration} 对象转换为 {@link ImmutableList} 对象。
     *
     * @param enumeration 待转换的 {@link Enumeration} 对象
     * @param <T>         迭代器中元素的类型
     * @return 转换后的 {@link ImmutableList} 对象
     */
    public static <T> ImmutableList<T> of(Enumeration<T> enumeration) {
        return of(IterableItr.of(enumeration));
    }

    /**
     * 将 {@link Stream} 对象转换为 {@link ImmutableList} 对象。
     *
     * @param stream 待转换的 {@link Stream} 对象
     * @param <T>    迭代器中元素的类型
     * @return 转换后的 {@link ImmutableList} 对象
     */
    public static <T> ImmutableList<T> of(Stream<T> stream) {
        return of(IterableItr.of(stream));
    }


    /**
     * 返回元素个数
     *
     * @return 元素个数
     */
    public int size() {
        return size;
    }

    /**
     * 判断是否为空列表
     *
     * @return 如果列表为空，则返回 true；否则返回 false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 判断元素是否在列表中
     *
     * @param element 待测试的元素
     * @return 如果元素在列表中，则返回 true；否则返回 false
     */
    public boolean contains(T element) {
        for (Object t : elements) {
            if (Objects.equals(element, t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回指定位置的元素
     *
     * @param index 元素的索引，支持负数，表示从后往前计算的位置
     * @return 指定位置的元素
     * @throws IndexOutOfBoundsException 如果索引越界，则抛出异常
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {

        if (index < 0) {
            index = size + index;
        }
        return (T) elements[index];
    }


    @Override
    public String toString() {
        return Arrays.toString(elements);
    }


    /**
     * 返回一个迭代器，如果列表不为空，则返回 {@link ArrayItr}；
     * 否则返回 {@link NoneItr}
     *
     * @return 迭代器
     */
    @SuppressWarnings("unchecked")
    @Override
    public Iterator<T> iterator() {
        return IterableItr.ofs((T[]) elements);
    }

    /**
     * 将列表转换为 {@link java.util.ArrayList} 形式
     *
     * @return {@link java.util.ArrayList}
     */
    @SuppressWarnings("unchecked")
    public ArrayList<T> toList() {
        return CollectionUtils.toList((T[]) elements);
    }

    /**
     * @return 返回一个包含所有元素的数组
     */
    @SuppressWarnings("unchecked")
    public T[] toArray(Class<T> type) {
        return ArrayUtils.toArray(type, (T[]) elements);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableList<?> that = (ImmutableList<?>) o;
        return size == that.size && Arrays.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(elements);
        return result;
    }


    private static class NoneImmutableList<T> extends ImmutableList<T> {

        private NoneImmutableList() {
            super(new Object[0]);
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean contains(T element) {
            return false;
        }

        @Override
        public T get(int index) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        public Iterator<T> iterator() {
            return NoneItr.of();
        }

        @Override
        public ArrayList<T> toList() {
            return new ArrayList<>();
        }

        @Override
        public T[] toArray(Class<T> type) {
            return ClassUtil.newWrapperArray(type, 0);
        }

        @Override
        public boolean equals(Object o) {
            return o == this;
        }


    }

}
