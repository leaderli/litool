package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.lang.Shell;
import io.leaderli.litool.core.text.StringUtils;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * LiConstant类包含了一些常量和方法，用于支持LiTool工具的实现
 */
public class LiConstant {

    /**
     * {@link Shell} 实例的默认bash路径
     */
    public static final String BASH = "sh";

    /**
     * {@link StringUtils#join(String, Iterable)}、
     * {@link StringUtils#join(String, Iterator)}、
     * {@link StringUtils#join(String, Object...)} 以及
     * {@link StringUtils#join0(String, Object[])} 方法中使用的默认分隔符
     */
    public static final String JOIN_DELIMITER = ",";

    /**
     * 用于表示lambda表达式的属性字段的前缀
     */
    public static final String LAMBDA_FIELD_PREFIX = "arg$";

    /**
     * 内部类由JVM创建的引用外部类的字段，如果手动定义一个名为{@code this$0}的字段，
     * JVM将会选择{@code this$0$}
     */
    public static final String INNER_CLASS_THIS_FIELD = "this$0";

    /**
     * 当发生异常时的默认消费者，适用于以下方法：
     *
     * @see Lino#throwable_map(ThrowableFunction)
     * @see Lira#throwable_map(ThrowableFunction)
     */
    @SuppressWarnings("all")
    public static Consumer<Throwable> WHEN_THROW = Throwable::printStackTrace;

    /**
     * 使用默认异常消费者接受一个异常
     *
     * @param e 异常
     */
    public static void whenThrow(Throwable e) {
        if (WHEN_THROW != null) {
            WHEN_THROW.accept(e);
        }
    }

    /**
     * 此方法仅供JUnit测试使用，用于清除默认异常消费者，执行代码，
     * 然后重新设置默认消费者
     *
     * @param runnable 执行的操作
     * @see #clear_when_throw()
     * @see #reset()
     */
    public static void temporary(Runnable runnable) {
        clear_when_throw();
        runnable.run();
        reset();
    }

    /**
     * 清除默认错误消费者
     */
    public static void clear_when_throw() {
        WHEN_THROW = null;
    }

    /**
     * 重置默认错误消费者
     */
    public static void reset() {
        WHEN_THROW = Throwable::printStackTrace;
    }
}
