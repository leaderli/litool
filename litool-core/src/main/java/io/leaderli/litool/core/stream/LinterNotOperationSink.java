package io.leaderli.litool.core.stream;

public interface LinterNotOperationSink<T> {
    LinterOperationSink<T> not();
}
