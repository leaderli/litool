package io.leaderli.litool.core.exception;

import io.leaderli.litool.core.text.StringUtils;

/**
 * @author leaderli
 * @since 2022/8/7
 */
public class ExceptionUtil {


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

        if (cause != null) {
            return cause.getStackTrace()[0] + " " + cause;
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
        while (throwable != null) {
            if (StringUtils.equals(throwout.getName(), throwable.getStackTrace()[0].getClassName())) {

                return throwable;
            }

            throwable = throwable.getCause();
        }
        return null;

    }
}
