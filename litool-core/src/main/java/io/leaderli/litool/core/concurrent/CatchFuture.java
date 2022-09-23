package io.leaderli.litool.core.concurrent;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * a {@link  Future} implement that catch the error of {@link #get()}, {@link #get(long, TimeUnit)}
 *
 * @author leaderli
 * @since 2022/9/24
 */
public interface CatchFuture<T> extends Future<T> {


    @Override
    T get();

    @Override
    T get(long timeout, TimeUnit unit);
}
