package io.leaderli.litool.core.exception;

import io.leaderli.litool.core.text.StringUtils;

/**
 * @author leaderli
 * @since 2022/8/7
 */
public class ExceptionUtil {


/**
 * Return the origin of throwable
 *
 * @param throwable a throwable
 * @return the origin of throwable
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


/**
 * Return the throwable at specific class throw out
 *
 * @param throwable a throwable
 * @param throwout  the class where throwable throw out
 * @return the throwable at specific class throw out
 */
public static Throwable getCause(Throwable throwable, Class<?> throwout) {
    if (throwout == null) {
        return null;
    }
    while (throwable != null) {
        if (StringUtils.equals(throwout.getName(), throwable.getStackTrace()[0].getClassName())) {

            return throwable;
        }

        throwable = throwable.getCause();
    }
    return null;

}
}
