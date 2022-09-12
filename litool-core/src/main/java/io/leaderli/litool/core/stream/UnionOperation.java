package io.leaderli.litool.core.stream;

import java.util.function.Function;

public interface UnionOperation<T> extends Function<T, Boolean> {
    CombineOperation<T> and();

    CombineOperation<T> or();
}
