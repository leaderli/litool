package io.leaderli.litool.core.meta.ra;

import java.util.Objects;
import java.util.function.Function;

public interface NullableFunction<T, R> extends Function<T, R> {

    NullableFunction<Object, Object> NOT_NULL = o -> true;
    NullableFunction<Object, Object> IS_NULL = Objects::isNull;

    @SuppressWarnings("unchecked")
    static <T, R> NullableFunction<T, R> notNull() {

        return (NullableFunction<T, R>) NOT_NULL;
    }

    @SuppressWarnings("unchecked")
    static <T, R> NullableFunction<T, R> isNull() {

        return (NullableFunction<T, R>) IS_NULL;
    }
}
