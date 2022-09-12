package io.leaderli.litool.core.meta.logic;

import java.util.function.Function;

/**
 * provide and, or, apply operation, and  after this only allow not,test operation.
 * so it will return {@link  CombineOperation}
 *
 * @param <T> the type of test value {@link  UnionOperation#apply(Object)}
 */
public interface UnionOperation<T> extends Function<T, Boolean> {
    /**
     * like logical operator and
     *
     * @return {@link  AndSome}
     */
    CombineOperation<T> and();

    /**
     * like logical operator or
     *
     * @return {@link  OrSome}
     */
    CombineOperation<T> or();
}
