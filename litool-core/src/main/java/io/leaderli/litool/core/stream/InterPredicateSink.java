package io.leaderli.litool.core.stream;

import java.util.function.Function;

public interface InterPredicateSink<T> extends Function<T, Boolean> {
    InterCombineOperationSink<T> and();

    InterCombineOperationSink<T> or();
}
