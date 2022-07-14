package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.type.LiReflectUtil;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * @author leaderli
 * @since 2022/6/25
 */
public class LiPrintUtil {


    /**
     * 快速打印多个参数值，使用空格分割
     *
     * @param args 参数
     */
    public static void print(Iterable<?> args) {


        print0(null, args);

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
     * 快速打印多个参数值，默认使用空格分割
     *
     * @param delimiter 分割符
     * @param args      参数
     */
    public static void print0(String delimiter, Object... args) {
        System.out.println(LiStrUtil.join(Lino.of(delimiter).get(" "), args));
    }

    /**
     * 快速打印多个参数值，使用换行分割
     *
     * @param args 参数
     */
    public static void println(Iterable<?> args) {


        print0("\n", args);

    }

    /**
     * 快速打印多个参数值，使用换行符作为分割
     *
     * @param args 参数
     */
    public static void println(Object... args) {

        print0("\n", args);
    }

    /**
     * 快速打印多个参数值，使用 delimiter 分割，为了避免和 {@link #print(Object...)} 冲突，第一个参数使用 box 包装一下
     *
     * @param delimiter 分隔符
     * @param args      参数
     */
    private static void print0(String delimiter, Iterable<?> args) {

        System.out.println(LiStrUtil.join(Lino.of(delimiter).get(" "), args));
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


    public static String getFieldsToString(Object o) {

        return LiStrUtil.join(",", Lino.of(o).map(l -> l.getClass().getFields())

                .toLira(Field.class)
                .map(f -> f.getName() + " = " + LiReflectUtil.getFieldValue(o, f).get())
                .getRaw());


    }
}
