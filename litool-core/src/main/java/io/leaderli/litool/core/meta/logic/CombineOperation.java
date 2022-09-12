package io.leaderli.litool.core.meta.logic;

/**
 * provide a combine of {@link  NotOperation} and {@link TestOperation}
 *
 * @param <T> the type of test value {@link  UnionOperation#apply(Object)}
 */
public interface CombineOperation<T> extends TestOperation<T>, NotOperation<T> {

}
