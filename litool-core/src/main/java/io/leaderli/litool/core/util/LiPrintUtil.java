package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.Lino;

import java.text.MessageFormat;

/**
 * @author leaderli
 * @since 2022/6/25
 */
public class LiPrintUtil {


    /**
     * 快速打印多个参数值，默认使用空格，分割
     *
     * @param args 参数
     */
    public static void print(Object... args) {


        print(null, args);

    }

    /**
     * 快速打印多个参数值，使用 delimiter 分割，为了避免和 {@link #print(Object...)} 冲突，第一个参数使用 box 包装一下
     *
     * @param delimiter 分隔符
     * @param args      参数
     */
    public static void print(LiBox<String> delimiter, Object... args) {

        System.out.println(LiStrUtil.join(Lino.of(delimiter).map(LiBox::value).get(), args));
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
