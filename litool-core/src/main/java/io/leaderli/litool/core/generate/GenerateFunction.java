package io.leaderli.litool.core.generate;

public interface GenerateFunction<T, R> {

    default R apply() {
        throw new UnsupportedOperationException();
    }

    default R apply(T t) {
        throw new UnsupportedOperationException();
    }

    default R apply(T t1, T t2) {
        throw new UnsupportedOperationException();
    }

    default R apply(T t1, T t2, T t3) {
        throw new UnsupportedOperationException();
    }

    default R apply(T t1, T t2, T t3, T t4) {
        throw new UnsupportedOperationException();
    }
}
