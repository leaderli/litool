package io.leaderli.litool.core.util;

import io.leaderli.litool.core.text.StrPool;
import io.leaderli.litool.core.text.StringUtils;

import java.util.regex.Pattern;

/**
 * @author leaderli
 * @since 2022/7/20
 */
public class RegexUtil {

/**
 * 删除匹配的全部内容
 *
 * @param pattern 正则
 * @param content 被匹配的内容
 * @return 删除后剩余的内容
 */
public static String delAll(Pattern pattern, CharSequence content) {
    if (null == pattern || StringUtils.isBlank(content)) {
        return StringUtils.str(content);
    }

    return pattern.matcher(content).replaceAll(StrPool.EMPTY);
}


/**
 * 指定内容中是否有表达式匹配的内容
 *
 * @param pattern 编译后的正则模式
 * @param content 被查找的内容
 * @return 指定内容中是否有表达式匹配的内容
 * @since 3.3.1
 */
public static boolean contains(Pattern pattern, CharSequence content) {
    if (null == pattern || null == content) {
        return false;
    }
    return pattern.matcher(content).find();
}
}
