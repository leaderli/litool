package io.leaderli.litool.core.exception;

/**
 * @author leaderli
 * @since 2022/8/7
 */
public class ExceptionUtil {


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
}
