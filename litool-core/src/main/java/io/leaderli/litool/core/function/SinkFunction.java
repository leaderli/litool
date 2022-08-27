package io.leaderli.litool.core.function;

public interface SinkFunction<T, R> {

R apply(T request, R last);
}
