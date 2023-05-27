package io.leaderli.litool.core.function;

import io.leaderli.litool.core.event.ILiEventListener;

/**
 * 表示接受一个 Throwable 输入并执行操作的函数式接口
 *
 * @see ILiEventListener
 */
@FunctionalInterface
public interface OnError {


    /**
     * 当异常时执行该方法
     *
     * @param throwable 抛出的异常
     */
    void onError(Throwable throwable);

}
