package io.leaderli.litool.core.util;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.text.StrPool;
import io.leaderli.litool.core.text.StringUtils;

import java.io.PrintStream;
import java.text.MessageFormat;

/**
 * @author leaderli
 * @since 2022/6/25
 */
public class ConsoleUtil {


    public static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * the default print behavior, could be replace appropriate time
     */
    @SuppressWarnings("all")
    public static PrintStream CONSOLE = System.out;


    /**
     * Quickly print multiple parameter values, separated by spaces
     *
     * @param args the args
     */
    public static void print(Object... args) {

        print0(null, args);

    }

    /**
     * Quickly print multiple parameter values, separated by  delimiter
     *
     * @param delimiter the separated string
     * @param args      the args
     */
    public static void print0(String delimiter, Object... args) {
        if (StringUtils.isEmpty(delimiter)) {
            delimiter = StrPool.SPACE;
        }
        CONSOLE.println(StringUtils.join(delimiter, args));
    }

    /**
     * Quickly print multiple parameter values, separated by spaces
     *
     * @param args the args
     */
    public static void print(Iterable<?> args) {


        print0(null, args);

    }

    private static void print0(String delimiter, Iterable<?> args) {

        if (StringUtils.isEmpty(delimiter)) {
            delimiter = StrPool.SPACE;
        }
        CONSOLE.println(StringUtils.join(delimiter, args));
    }

    /**
     * Quickly println multiple parameter values, separated by  {@link  #LINE_SEPARATOR}
     *
     * @param args the args
     */
    public static void println(Iterable<?> args) {


        print0(LINE_SEPARATOR, args);

    }

    /**
     * Quickly println multiple parameter values, separated by  {@link  #LINE_SEPARATOR}
     *
     * @param args the args
     */
    public static void println(Object... args) {

        print0(LINE_SEPARATOR, args);
    }

    /**
     * simple print a line
     */
    public static void line() {

        CONSOLE.println(StringUtils.just("", 60, '-'));

    }


    /**
     * simple print a line
     *
     * @param logo the badge
     */
    public static void line(Object logo) {

        CONSOLE.println(StringUtils.just(logo.toString(), 60, '-'));

    }

    /**
     * print formatted message, format rule is {@link  MessageFormat#format(String, Object...)}
     *
     * @param pattern   the message format
     * @param arguments the placeholder variables
     * @see MessageFormat#format(String, Object...)
     */
    public static void print_format(String pattern, Object... arguments) {
        CONSOLE.println(MessageFormat.format(pattern, arguments));
    }

    /**
     * println the array
     *
     * @param arr the array
     */
    public static void printArray(Object arr) {
        CONSOLE.println(ArrayUtils.toString(arr));
    }

}
