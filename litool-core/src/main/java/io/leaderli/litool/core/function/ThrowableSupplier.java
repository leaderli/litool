package io.leaderli.litool.core.function;

/**
 * Represents  a supplier of results which may throw error
 *
 * @param <T> the type of results supplied by this supplier
 * @author leaderli
 * @since 2022/6/16
 */

@FunctionalInterface
public interface ThrowableSupplier<T> {
/**
 * Gets a result.
 *
 * @return a result
 * @throws Throwable error
 */
T get() throws Throwable;
}
