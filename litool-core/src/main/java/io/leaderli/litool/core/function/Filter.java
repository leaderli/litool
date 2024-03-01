package io.leaderli.litool.core.function;

import java.util.function.Function;

public interface Filter<T> extends Function<T, Boolean> {


    @SafeVarargs
    static <T> Filter<T> chain(Filter<T>... appender) {
        return t -> {
            for (Filter<T> filter : appender) {
                if (!filter.apply(t)) {
                    return false;
                }
            }
            return true;
        };

    }
}
