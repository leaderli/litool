package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.exception.LiThrowableFunction;

import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/7/12
 */
public class LiConstant {


    /**
     * 默认的异常消费者，适用于
     *
     * @see Lino#throwable_map(LiThrowableFunction)
     * @see Lira#throwable_map(LiThrowableFunction)
     */
    public static Consumer<Throwable> whenThrow = Throwable::printStackTrace;
}
