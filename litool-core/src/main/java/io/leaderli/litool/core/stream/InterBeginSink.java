package io.leaderli.litool.core.stream;

public interface InterBeginSink<T> {

    InterCombineOperationSink<T> begin();
}
