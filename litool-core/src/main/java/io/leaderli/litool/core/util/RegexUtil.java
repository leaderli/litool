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
     * @param pattern the compile regex
     * @param content the found string content
     * @return remove the match regex fragment
     */
    public static String delAll(Pattern pattern, CharSequence content) {
        if (null == pattern || StringUtils.isBlank(content)) {
            return StringUtils.str(content);
        }

        return pattern.matcher(content).replaceAll(StrPool.EMPTY);
    }


    /**
     * @param pattern the compile regex
     * @param content the found string content
     * @return the content have fragment match regex
     * @since 3.3.1
     */
    public static boolean contains(Pattern pattern, CharSequence content) {
        if (null == pattern || null == content) {
            return false;
        }
        return pattern.matcher(content).find();
    }
}
