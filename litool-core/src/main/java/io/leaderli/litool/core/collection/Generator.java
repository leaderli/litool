package io.leaderli.litool.core.collection;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * 该接口表示一个无限迭代器，即它永远不会停止生成元素。例如 {@link IntGenerator}。
 *
 * @param <T> 迭代器生成的元素类型
 * @see IntGenerator
 */
public interface Generator<T> extends IterableItr<T> {
    @Override
    default boolean hasNext() {
        return true;
    }

    @Override
    default Iterator<T> iterator() {
        return this;
    }

    @Override
    default String name() {
        return "Generator";
    }

    @Override
    default ArrayList<T> toList() {
        throw new UnsupportedOperationException("generator is infinity");
    }

    @Override
    default T[] toArray(Class<T> type) {
        throw new UnsupportedOperationException("generator is infinity");
    }

    @Override
    default Object[] toArray() {
        throw new UnsupportedOperationException("generator is infinity");
    }
}
