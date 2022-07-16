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
     * @see io.leaderli.litool.core.meta.reactor.ThrowableMapRa
     */
    public static Consumer<Throwable> WHEN_THROW = Throwable::printStackTrace;


    public static void accept(Throwable e) {

        if (WHEN_THROW != null) {
            WHEN_THROW.accept(e);
        }
    }
}
