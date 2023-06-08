package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.lang.Shell;
import io.leaderli.litool.core.text.StringUtils;

import java.util.Iterator;

/**
 * LiConstant类包含了一些常量和方法，用于支持LiTool工具的实现
 */
public class LiConstant {

    /**
     * {@link Shell} 实例的默认bash路径
     */
    public static final String BASH = "sh";

    /**
     * {@link StringUtils#join(String, Iterable)}、
     * {@link StringUtils#join(String, Iterator)}、
     * {@link StringUtils#join(String, Object...)} 以及
     * {@link StringUtils#join0(String, Object[])} 方法中使用的默认分隔符
     */
    public static final String JOIN_DELIMITER = ",";

    /**
     * 用于表示lambda表达式的属性字段的前缀
     */
    public static final String LAMBDA_FIELD_PREFIX = "arg$";

    /**
     * 内部类由JVM创建的引用外部类的字段，如果手动定义一个名为{@code this$0}的字段，
     * JVM将会选择{@code this$0$}
     */
    public static final String INNER_CLASS_THIS_FIELD = "this$0";

}
