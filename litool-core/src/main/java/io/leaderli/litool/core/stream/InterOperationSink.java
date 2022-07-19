package io.leaderli.litool.core.stream;

import java.util.function.Predicate;

public interface InterOperationSink<T> {
    InterPredicateSink<T> test(Predicate<T> predicate);
}
