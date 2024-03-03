package io.leaderli.litool.core.function;

public interface Chain<T> {

    Chain<T> addHead(T filter);

    Chain<T> add(T filter);
}
