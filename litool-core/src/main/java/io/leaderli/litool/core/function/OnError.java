package io.leaderli.litool.core.function;

import io.leaderli.litool.core.event.ILiEventListener;

import java.util.function.Consumer;

/**
 * 表示接受一个 Throwable 输入并执行操作的函数式接口
 *
 * @see ILiEventListener
 */
@FunctionalInterface
public interface OnError extends Consumer<Throwable> {

    /**
     * 当异常时执行该方法
     *
     * @param throwable 抛出的异常
     */
    void onError(Throwable throwable);

    @Override
    default void accept(Throwable throwable) {
        onError(throwable);
    }
}
