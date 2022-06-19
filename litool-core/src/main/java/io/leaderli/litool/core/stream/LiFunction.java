package io.leaderli.litool.core.stream;

public interface LiFunction<T, R> {

    R apply(T request, R last);
}
