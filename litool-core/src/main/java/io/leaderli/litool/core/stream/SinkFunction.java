package io.leaderli.litool.core.stream;

public interface SinkFunction<T, R> {

R apply(T request, R last);
}
