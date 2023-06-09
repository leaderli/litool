package io.leaderli.litool.core.collection;

/**
 * 转换为数组
 *
 * @param <T> 数组类型
 */
public interface ToArray<T> {
    /**
     * @param type 数组类型
     * @return 返回一个包含所有元素的新的数组
     */
    T[] toArray(Class<T> type);
}
