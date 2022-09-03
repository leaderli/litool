package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/9/3
 */
public interface LiraGenerator<T> {


    boolean hasNext(int state);

    T next();
}
