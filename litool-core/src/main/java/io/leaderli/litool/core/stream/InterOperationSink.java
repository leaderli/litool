package io.leaderli.litool.core.stream;

import java.util.function.Function;

@FunctionalInterface
public interface InterOperationSink<T> {
    UnionOperation<T> test(Function<T, ?> predicate);
}
