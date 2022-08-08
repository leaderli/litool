package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.function.ThrowableFunction;

import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/7/12
 */
public class LiConstant {

    public static final String ENTRY_NAME_RULE = "[a-zA-Z0-9_]+";
    /**
     * 默认的异常消费者，适用于
     *
     * @see Lino#throwable_map(ThrowableFunction)
     * @see Lira#throwable_map(ThrowableFunction)
     * @see io.leaderli.litool.core.meta.ra.ThrowableMapRa
     */
    public static Consumer<Throwable> WHEN_THROW = Throwable::printStackTrace;

    public static void accept(Throwable e) {

        if (WHEN_THROW != null) {
            WHEN_THROW.accept(e);
        }
    }

    /**
     * 在函数执行前将默认异常关闭，执行后复位
     *
     * @param runnable 执行函数
     * @see #clear()
     * @see #reset()
     */
    public static void temporary(Runnable runnable) {
        clear();
        runnable.run();
        reset();

    }

    /**
     * 清除异常处理
     */
    public static void clear() {
        WHEN_THROW = null;
    }

    /**
     * 恢复默认异常处理
     */
    public static void reset() {
        WHEN_THROW = Throwable::printStackTrace;
    }
}
