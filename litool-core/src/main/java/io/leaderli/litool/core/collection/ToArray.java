package io.leaderli.litool.core.collection;

public interface ToArray<T> {
    /**
     * @return 返回一个包含所有元素的新的数组
     */
    T[] toArray(Class<T> type);
}
