package io.leaderli.litool.core.lang;

/**
 * Performs an comparable at an specific type of {@code T}
 *
 * @param <T> the type of objects compare to
 * @author leaderli
 * @since 2022/8/13 1:18 PM
 */
@FunctionalInterface
public interface EqualComparator<T> {
/**
 * two parameter are equivalent in some way
 *
 * @param left  the left to compare
 * @param right the  right to compare
 * @return left and right are equivalent in some way
 */
boolean apply(T left, T right);
}
