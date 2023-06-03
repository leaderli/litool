package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.function.Function;
import io.leaderli.litool.core.lang.Shell;
import io.leaderli.litool.core.text.StringUtils;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/7/12
 */
public class LiConstant {


    /**
     * the bash path used for {@link  Shell}
     */
    public static final String BASH = "sh";
    /**
     * the default delimiter of
     * {@link  StringUtils#join(String, Iterable)}
     * {@link  StringUtils#join(String, Iterator)}
     * {@link  StringUtils#join(String, Object...)}
     * {@link  StringUtils#join0(String, Object[])}
     */
    public static final String JOIN_DELIMITER = ",";


    /**
     * A prefix for a field that represents a property of a lambda expression.
     */
    public static final String LAMBDA_FIELD_PREFIX = "arg$";

    /**
     * the field of inner class create by jvm that reference the out class,
     * if you manually define a field named {@code  this$0}, the jvm will
     * choose {@code this$0$}
     */
    public static final String INNER_CLASS_THIS_FIELD = "this$0";
    /**
     * The default consumer when exception occurs, it suitable for
     *
     * @see Lino#throwable_map(Function)
     * @see Lira#throwable_map(Function)
     */
    @SuppressWarnings("all")
    public static Consumer<Throwable> WHEN_THROW = Throwable::printStackTrace;

    public static void accept(Throwable e) {

        if (WHEN_THROW != null) {
            WHEN_THROW.accept(e);
        }
    }

    /**
     * the action only provide for junit test, it clear the default
     * exception consumer, and run the code, and then reset the default
     * consumer back
     *
     * @param runnable the action
     * @see #clear_when_throw()
     * @see #reset()
     */
    public static void temporary(Runnable runnable) {
        clear_when_throw();
        runnable.run();
        reset();

    }

    /**
     * clear the default error consumer
     */
    public static void clear_when_throw() {
        WHEN_THROW = null;
    }

    /**
     * reset the default error consumer
     */
    public static void reset() {
        WHEN_THROW = Throwable::printStackTrace;
    }
}
