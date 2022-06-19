package io.leaderli.litool.core.stream;

import java.util.function.Predicate;

public interface LinterOperationSink<T> {
    LinterPredicateSink<T> test(Predicate<T> predicate);
}
