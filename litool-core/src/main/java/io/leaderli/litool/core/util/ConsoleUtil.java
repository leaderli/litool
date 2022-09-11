package io.leaderli.litool.core.util;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.text.StrPool;
import io.leaderli.litool.core.text.StringUtils;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Arrays;

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
    public static void print(Iterable<?> args) {


        print0(null, args);

    }

    /**
     * 快速打印多个参数值，使用 delimiter 分割，为了避免和 {@link #print(Object...)} 冲突，第一个参数使用 box 包装一下
     *
     * @param delimiter 分隔符
     * @param args      参数
     */
    private static void print0(String delimiter, Iterable<?> args) {

        if (StringUtils.isEmpty(delimiter)) {
            delimiter = StrPool.SPACE;
        }
        CONSOLE.println(StringUtils.join(delimiter, args));
    }

    /**
     * 快速打印多个参数值，使用空格分割
     *
     * @param args 参数
     */
    public static void print(Object... args) {


        print0(null, Arrays.asList(args));

    }

    /**
     * 快速打印多个参数值，使用换行分割
     *
     * @param args 参数
     */
    public static void println(Iterable<?> args) {


        print0(LINE_SEPARATOR, args);

    }

    /**
     * 打印横线
     */
    public static void line() {

        CONSOLE.println(StringUtils.just("", 60, '-'));

    }


    /**
     * 快速打印多个参数值，使用换行符作为分割
     *
     * @param args 参数
     */
    public static void println(Object... args) {

        print0(LINE_SEPARATOR, args);
    }

    /**
     * 快速打印多个参数值，默认使用空格分割
     *
     * @param delimiter 分割符
     * @param args      参数
     */
    public static void print0(String delimiter, Object... args) {
        if (StringUtils.isEmpty(delimiter)) {
            delimiter = StrPool.SPACE;
        }
        CONSOLE.println(StringUtils.join(delimiter, args));
    }

    /**
     * 消息模板中使用 {0} {1} 占位符， 实际打印的消息中占位符会被指定 index 的参数替换
     *
     * @param pattern   消息模板
     * @param arguments 参数
     * @see MessageFormat#format(String, Object...)
     */
    public static void print_format(String pattern, Object... arguments) {
        CONSOLE.println(MessageFormat.format(pattern, arguments));
    }

    public static void printArray(Object arr) {
        System.out.println(ArrayUtils.toString(arr));
    }

}
