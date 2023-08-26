
package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.text.StrPool;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    /**
     * @see #replaceAll(String, Pattern, String) 替换位空字符串
     */
    public static String removeAll(final String text, final Pattern regex) {
        return replaceAll(text, regex, StrPool.EMPTY);
    }

    /**
     * @see #replaceAll(String, String, String) 替换位空字符串
     */
    public static String removeAll(final String text, final String regex) {
        return replaceAll(text, regex, StrPool.EMPTY);
    }

    /**
     * <p>使用给定的正则表达式模式替换文本字符串中匹配的每个子字符串。</p>
     *
     * <p>此方法是{@code null}安全等效于:
     * <ul>
     *  <li>{@code pattern.matcher(text).replaceAll(replacement)}</li>
     * </ul>
     *
     * <p>传递给此方法的{@code null}引用无操作。</p>
     *
     * <pre>
     * RegExUtils.replaceAll(null, *, *)       = null
     * RegExUtils.replaceAll("any", (Pattern) null, *)   = "any"
     * RegExUtils.replaceAll("any", *, null)   = "any"
     * RegExUtils.replaceAll("", Pattern.compile(""), "zzz")    = "zzz"
     * RegExUtils.replaceAll("", Pattern.compile(".*"), "zzz")  = "zzz"
     * RegExUtils.replaceAll("", Pattern.compile(".+"), "zzz")  = ""
     * RegExUtils.replaceAll("abc", Pattern.compile(""), "ZZ")  = "ZZaZZbZZcZZ"
     * RegExUtils.replaceAll("<__>\n<__>", Pattern.compile("<.*>"), "z")                 = "z\nz"
     * RegExUtils.replaceAll("<__>\n<__>", Pattern.compile("<.*>", Pattern.DOTALL), "z") = "z"
     * RegExUtils.replaceAll("<__>\n<__>", Pattern.compile("(?s)<.*>"), "z")             = "z"
     * RegExUtils.replaceAll("ABCabc123", Pattern.compile("[a-z]"), "_")       = "ABC___123"
     * RegExUtils.replaceAll("ABCabc123", Pattern.compile("[^A-Z0-9]+"), "_")  = "ABC_123"
     * RegExUtils.replaceAll("ABCabc123", Pattern.compile("[^A-Z0-9]+"), "")   = "ABC123"
     * RegExUtils.replaceAll("Lorem ipsum  dolor   sit", Pattern.compile("( +)([a-z]+)"), "_$2")  = "Lorem_ipsum_dolor_sit"
     * </pre>
     *
     * @param text        要搜索和替换的文本,可以为null
     * @param regex       要匹配此字符串的正则表达式模式
     * @param replacement 要为每个匹配项替换的字符串
     * @return 已处理的文本, 如果输入为null字符串, 则返回{@code null}
     * @see java.util.regex.Matcher#replaceAll(String)
     * @see Pattern
     */
    public static String replaceAll(final String text, final Pattern regex, final String replacement) {
        if (text == null || regex == null || replacement == null) {
            return text;
        }
        return regex.matcher(text).replaceAll(replacement);
    }


    /**
     * @see #replaceAll(String, Pattern, String)
     */
    public static String replaceAll(final String text, final String regex, final String replacement) {
        if (text == null || regex == null || replacement == null) {
            return text;
        }
        return text.replaceAll(regex, replacement);
    }

    /**
     * @see #replaceFirst(String, String, String) 替换位空字符串
     */
    public static String removeFirst(final String text, final Pattern regex) {
        return replaceFirst(text, regex, StrPool.EMPTY);
    }

    public static String replaceFirst(final String text, final Pattern regex, final String replacement) {
        if (text == null || regex == null || replacement == null) {
            return text;
        }
        return regex.matcher(text).replaceFirst(replacement);
    }

    /**
     * @see #replaceFirst(String, Pattern, String) 替换位空字符串
     */
    public static String removeFirst(final String text, final String regex) {
        return replaceFirst(text, regex, StrPool.EMPTY);
    }

    /**
     * @see String#replaceFirst(String, String)
     */
    public static String replaceFirst(final String text, final String regex, final String replacement) {
        if (text == null || regex == null || replacement == null) {
            return text;
        }
        return text.replaceFirst(regex, replacement);
    }

    /**
     * @see #replacePattern(String, String, String)  替换位空字符串
     */
    public static String removePattern(final String text, final String regex) {
        return replacePattern(text, regex, StrPool.EMPTY);
    }

    /**
     * <p>Replaces each substring of the source String that matches the given regular expression with the given
     * replacement using the {@link Pattern#DOTALL} option. DOTALL is also known as single-line mode in Perl.</p>
     * <p>
     * This call is a {@code null} safe equivalent to:
     * <ul>
     * <li>{@code text.replaceAll(&quot;(?s)&quot; + regex, replacement)}</li>
     * <li>{@code Pattern.compile(regex, Pattern.DOTALL).matcher(text).replaceAll(replacement)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * RegExUtils.replacePattern(null, *, *)       = null
     * RegExUtils.replacePattern("any", (String) null, *)   = "any"
     * RegExUtils.replacePattern("any", *, null)   = "any"
     * RegExUtils.replacePattern("", "", "zzz")    = "zzz"
     * RegExUtils.replacePattern("", ".*", "zzz")  = "zzz"
     * RegExUtils.replacePattern("", ".+", "zzz")  = ""
     * RegExUtils.replacePattern("&lt;__&gt;\n&lt;__&gt;", "&lt;.*&gt;", "z")       = "z"
     * RegExUtils.replacePattern("ABCabc123", "[a-z]", "_")       = "ABC___123"
     * RegExUtils.replacePattern("ABCabc123", "[^A-Z0-9]+", "_")  = "ABC_123"
     * RegExUtils.replacePattern("ABCabc123", "[^A-Z0-9]+", "")   = "ABC123"
     * RegExUtils.replacePattern("Lorem ipsum  dolor   sit", "( +)([a-z]+)", "_$2")  = "Lorem_ipsum_dolor_sit"
     * </pre>
     *
     * @param text        the source string
     * @param regex       the regular expression to which this string is to be matched
     * @param replacement the string to be substituted for each match
     * @return The resulting {@code String}
     * @see #replaceAll(String, String, String)
     * @see String#replaceAll(String, String)
     * @see Pattern#DOTALL
     */
    public static String replacePattern(final String text, final String regex, final String replacement) {
        if (text == null || regex == null || replacement == null) {
            return text;
        }
        return Pattern.compile(regex, Pattern.DOTALL).matcher(text).replaceAll(replacement);
    }


    /**
     * @param text            源字符串
     * @param regex           正则表达式，不需要额外添加括号，方法内部会自动添加匹配组的括号
     * @param replaceFunction 以正则表达式匹配到的组1作为参数，以函数执行结果替换组1
     * @return 替换正则表达式匹配的字符串
     */
    public static String replacePatternByFunction(final String text, final String regex, Function<String, String> replaceFunction) {
        if (text == null || regex == null || replaceFunction == null) {
            return text;
        }
        Pattern pattern = Pattern.compile("(" + regex + ")");
        Matcher matcher = pattern.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String match = matcher.group(1); // 获取匹配的字符串
            String replacement = replaceFunction.apply(match);
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
