package io.leaderli.litool.core.util;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author leaderli
 * @since 2022/6/25
 */
public class LiPrintUtil {


//    public static void print(String... args) {
//
//        System.out.println(String.join(" ,", args));
//    }

    /**
     * 快速打印多个参数值，默认使用空格，分割
     *
     * @param args 参数
     */
    public static void print(Object... args) {


        print(" ", args);

    }

    /**
     * 快速打印多个参数值，使用 delimiter 分割
     *
     * @param delimiter 分隔符
     * @param args      参数
     */
    public static void print(String delimiter, Object... args) {


        if (args != null) {
            System.out.println(Arrays.stream(args).map(String::valueOf).collect(Collectors.joining(delimiter)));
        }
    }

    /**
     * 消息模板中使用 {0} {1} 占位符， 实际打印的消息中占位符会被指定 index 的参数替换
     *
     * @param pattern   消息模板
     * @param arguments 参数
     * @see MessageFormat#format(String, Object...)
     */
    public static void print_format(String pattern, Object... arguments) {

        System.out.println(MessageFormat.format(pattern, arguments));
    }
}
