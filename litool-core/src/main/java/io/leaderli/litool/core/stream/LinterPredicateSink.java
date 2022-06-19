package io.leaderli.litool.core.stream;

import java.util.function.Function;

public interface LinterPredicateSink<T> extends Function<T,Boolean> {
    LinterCombineOperationSink<T> and();
    LinterCombineOperationSink<T> or();
}
