package io.leaderli.litool.core.exception;

import io.leaderli.litool.core.io.StringWriter;
import io.leaderli.litool.core.text.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/8/7
 */
public class ExceptionUtil {


    /**
     * 返回多次反射调用前的原始报错
     * <p>
     *
     * @see java.lang.reflect.Method#invoke(Object, Object...)
     */
    public static Throwable unwrapThrowable(Throwable wrapped) {
        Throwable unwrapped = wrapped;
        while (true) {
            if (unwrapped instanceof InvocationTargetException) {
                unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
            } else if (unwrapped instanceof UndeclaredThrowableException) {
                unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
            } else {
                return unwrapped;
            }
        }
    }

    /**
     * 获取异常的最底层原因
     *
     * @param throwable 异常对象
     * @return 最底层的异常对象
     */
    public static Throwable getCause(Throwable throwable) {

        while (throwable != null) {
            Throwable cause = throwable.getCause();

            if (cause == null) {
                return throwable;
            }
            throwable = cause;
        }
        return null;
    }

    public static String beauty(Throwable throwable) {
        Throwable cause = getCause(throwable);

        if (cause != null && cause.getStackTrace().length > 0) {
            return cause.getStackTrace()[0] + " " + cause;
        }
        return "";
    }

    /**
     * @param throwable 错误信息
     * @return 将错误信息转换为字符串返回
     * @see Throwable#printStackTrace()
     */
    public static String getStackTrace(Throwable throwable) {
        if (throwable != null) {
            StringWriter stringWriter = new StringWriter();
            throwable.printStackTrace(stringWriter.printStream());
            return stringWriter.get();
        }
        return "";
    }


    /**
     * 获取在指定类中抛出的异常
     *
     * @param throwable 异常对象
     * @param throwout  抛出异常的类
     * @return 在指定类中抛出的异常对象
     */
    public static Throwable getCause(Throwable throwable, Class<?> throwout) {
        if (throwout == null) {
            return null;
        }
        while (throwable != null && throwable.getStackTrace().length > 0) {
            if (StringUtils.equals(throwout.getName(), throwable.getStackTrace()[0].getClassName())) {

                return throwable;
            }

            throwable = throwable.getCause();
        }
        return null;

    }

    public static <T> T supplier(Supplier<? extends T> supplier, Supplier<? extends T> def) {
        try {
            return supplier.get();
        } catch (Throwable ignore) {
        }
        return def.get();
    }

    public static <T> T supplier(Supplier<? extends T> supplier, T def) {
        try {
            return supplier.get();
        } catch (Throwable ignore) {
        }
        return def;
    }

    public static <T, R> R function(Function<? super T, ? extends R> supplier, T arg, Function<? super T, ? extends R> def) {
        try {
            return supplier.apply(arg);
        } catch (Throwable ignore) {
        }
        return def.apply(arg);
    }

    public static <T, R> R function(Function<? super T, ? extends R> supplier, T arg, R def) {
        try {
            return supplier.apply(arg);
        } catch (Throwable ignore) {
        }
        return def;
    }
}
