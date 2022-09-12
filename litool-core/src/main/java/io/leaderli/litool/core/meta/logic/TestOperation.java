package io.leaderli.litool.core.meta.logic;

import java.util.function.Function;

/**
 * provide test operation, and after this should only use  and, or, apply operation,
 * so it will return {@link  UnionOperation}
 *
 * @param <T> the type of test value {@link  UnionOperation#apply(Object)}
 */
@FunctionalInterface
public interface TestOperation<T> {
    /**
     * @param predicate the test function , the result will parse to boolean by
     *                  {@link  io.leaderli.litool.core.util.BooleanUtil#parse(Object)}
     * @return {@link  TestSome}
     */
    UnionOperation<T> test(Function<T, ?> predicate);
}
