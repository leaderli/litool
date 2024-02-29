package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.function.Filter;

import java.util.Arrays;

public class ArrayEqual<T> implements Filter<T[]> {


    public final T[] arr;

    public ArrayEqual(T[] arr) {
        this.arr = arr;
    }

    @SafeVarargs
    public static <T> ArrayEqual<T> of(T... arr) {
        return new ArrayEqual<>(arr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayEqual<?> that = (ArrayEqual<?>) o;
        return Arrays.equals(arr, that.arr);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(arr);
    }

    @Override
    public Boolean apply(T[] compare) {
        return Arrays.equals(arr, compare);
    }
}
