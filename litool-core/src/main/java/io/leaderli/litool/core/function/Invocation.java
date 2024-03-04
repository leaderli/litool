package io.leaderli.litool.core.function;

public interface Invocation<T> {

    T proceed() throws Throwable;
}
