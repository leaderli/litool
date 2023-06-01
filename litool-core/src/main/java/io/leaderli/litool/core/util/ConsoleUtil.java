package io.leaderli.litool.core.util;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.text.StrPool;
import io.leaderli.litool.core.text.StringUtils;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Iterator;

/**
 * 控制台输出工具类
 */
public class ConsoleUtil {

    /**
     * 换行符
     */
    public static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * 默认的输出流，可以在适当的时候替换
     */
    @SuppressWarnings("all")
    public static PrintStream CONSOLE = System.out;

    /**
     * 打印多个参数值，用空格分隔
     *
     * @param args 参数值列表
     */
    public static void print(Object... args) {
        print0(null, args);
    }

    /**
     * 打印多个参数值，用空格分隔
     *
     * @param args 参数值列表
     */
    public static void print(Iterable<?> args) {
        print0(null, args);
    }

    /**
     * 打印多个参数值，用空格分隔
     *
     * @param args 参数值列表
     */
    public static void print(Iterator<?> args) {
        print0(null, args);
    }

    /**
     * 打印多个参数值，用指定的分隔符分隔
     *
     * @param delimiter 分隔符，默认使用 {@link StrPool#SPACE}
     * @param args      参数值列表
     */
    public static void print0(String delimiter, Object... args) {
        if (StringUtils.isEmpty(delimiter)) {
            delimiter = StrPool.SPACE;
        }
        CONSOLE.println(StringUtils.join(delimiter, args));
    }

    /**
     * 打印多个参数值，用指定的分隔符分隔
     *
     * @param delimiter 分隔符
     * @param args      参数值列表
     */
    public static void print0(String delimiter, Iterator<?> args) {
        if (StringUtils.isEmpty(delimiter)) {
            delimiter = StrPool.SPACE;
        }
        CONSOLE.println(StringUtils.join(delimiter, args));
    }

    /**
     * 打印多个参数值，用指定的分隔符分隔
     *
     * @param delimiter 分隔符
     * @param args      参数值列表
     */
    public static void print0(String delimiter, Iterable<?> args) {
        if (StringUtils.isEmpty(delimiter)) {
            delimiter = StrPool.SPACE;
        }
        CONSOLE.println(StringUtils.join(delimiter, args));
    }

    /**
     * 打印多个参数值，用换行符分隔
     *
     * @param args 参数值列表
     */
    public static void println(Object... args) {
        print0(LINE_SEPARATOR, args);
    }

    /**
     * 打印多个参数值，用换行符分隔
     *
     * @param args 参数值列表
     */
    public static void println(Iterable<?> args) {
        print0(LINE_SEPARATOR, args);
    }

    /**
     * 打印多个参数值，用换行符分隔
     *
     * @param args 参数值列表
     */
    public static void println(Iterator<?> args) {
        print0(LINE_SEPARATOR, args);
    }

    private static int LINE_COUNT;

    /**
     * 打印一行横线
     */
    public static void line() {
        CONSOLE.println(StringUtils.just(" " + ++LINE_COUNT + " ", 80, '-'));
    }

    /**
     * 打印一行横线，正中间为logo
     *
     * @param logo -
     */
    public static void line(Object logo) {
        CONSOLE.println(StringUtils.just(" " + logo + " ", 80, '-'));
    }

    /**
     * 格式化打印消息，格式规则参考 {@link MessageFormat#format(String, Object...)}
     *
     * @param pattern   消息格式
     * @param arguments 占位符变量
     */
    public static void print_format(String pattern, Object... arguments) {
        CONSOLE.println(MessageFormat.format(pattern, arguments));
    }

    /**
     * 打印数组
     *
     * @param arr 数组
     */
    public static void printArray(Object arr) {
        CONSOLE.println(ArrayUtils.toString(arr));
    }
}
