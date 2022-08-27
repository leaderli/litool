package io.leaderli.litool.core.lang;

/**
 * @author leaderli
 * @since 2022/8/13 1:18 PM
 */
public interface EqualComparator<T> {
boolean apply(T left, T right);
}
