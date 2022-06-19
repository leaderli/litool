package io.leaderli.litool.core.stream;

public interface LinterBeginSink<T> {

    LinterCombineOperationSink<T> begin();
}
