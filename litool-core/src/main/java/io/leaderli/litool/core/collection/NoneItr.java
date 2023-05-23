package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.type.ClassUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * NoneItr 是一个没有元素的 IterableItr 的实现类。
 *
 * @param <T> IterableItr 迭代器中元素的类型
 * @author leaderli
 * @since 2022/7/18
 */
public class NoneItr<T> implements IterableItr<T> {
    /**
     * 所有 NoneItr 对象共享同一个实例
     */
    private static final io.leaderli.litool.core.collection.NoneItr<?> NONE = new io.leaderli.litool.core.collection.NoneItr<>();

    private NoneItr() {

    }


    /**
     * 判断是否还有下一个元素。
     *
     * @return false
     */
    @Override
    public boolean hasNext() {
        return false;
    }

    /**
     * 获取下一个元素。
     *
     * @return 不返回任何元素，总是抛出 NoSuchElementException 异常
     * @throws NoSuchElementException always throw
     */
    @Override
    public T next() {
        throw new NoSuchElementException();
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    /**
     * 返回 {@link  #NONE}
     *
     * @param <T> IterableItr 迭代器中元素的类型
     * @return {@link #NONE}
     */
    @SuppressWarnings("unchecked")
    public static <T> io.leaderli.litool.core.collection.NoneItr<T> of() {
        return (io.leaderli.litool.core.collection.NoneItr<T>) NONE;
    }


    @Override
    public String name() {
        return "NoneItr";
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
    public Object[] toArray() {
        return new Object[0];
    }


}
