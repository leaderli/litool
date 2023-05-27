package io.leaderli.litool.core.concurrent;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 表示一种 {@link Future} 的实现，重写了 {@link #get()} 和 {@link #get(long, TimeUnit)} 方法的，以去掉方法的异常描述符
 *
 * @param <T> Future 返回值的类型
 * @see Future
 */
public interface ErrorHandledFuture<T> extends Future<T> {

    @Override
    T get();

    @Override
    T get(long timeout, TimeUnit unit);
}
