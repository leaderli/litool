package io.leaderli.litool.core.lang;

/**
 * 对指定类型 {@code T} 的对象进行比较
 *
 * @param <T> 对象的类型
 * @author leaderli
 * @since 2022/8/13 1:18 PM
 */
@FunctionalInterface
public interface EqualComparator<T> {
    /**
     * 判断两个参数是否在某种程度上等价
     *
     * @param left  要比较的左侧对象
     * @param right 要比较的右侧对象
     * @return 如果 left 和 right 在某种程度上等价，则返回 true
     */
    boolean apply(T left, T right);
}
