package io.leaderli.litool.core.meta.logic;

/**
 * provide not operation, and after this only allow test operation.
 * so it will return {@link TestOperation}
 *
 * @param <T> the type of test value {@link  UnionOperation#apply(Object)}
 */
@FunctionalInterface
public interface NotOperation<T> {
    /**
     * negate nest test operation
     *
     * @return a  {@link  NotSome}
     */
    TestOperation<T> not();
}
