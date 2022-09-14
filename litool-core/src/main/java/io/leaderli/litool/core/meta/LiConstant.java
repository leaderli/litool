package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.text.StringUtils;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/7/12
 */
public class LiConstant {


    /**
     * the default delimiter of
     * {@link  StringUtils#join(String, Iterable)}
     * {@link  StringUtils#join(String, Iterator)}
     * {@link  StringUtils#join(String, Object...)}
     * {@link  StringUtils#join0(String, Object[])}
     */
    public static final String JOIN_DELIMITER = ",";
    public static final String ATTRIBUTE_NAME_RULE = "[a-zA-Z0-9_]+";
    /**
     * The default consumer when exception occurs, it suitable for
     *
     * @see Lino#throwable_map(ThrowableFunction)
     * @see Lira#throwable_map(ThrowableFunction)
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
