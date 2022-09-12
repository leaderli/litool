package io.leaderli.litool.core.stream;

@FunctionalInterface
public interface NotOperation<T> {
    InterOperationSink<T> not();
}
