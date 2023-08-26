package io.leaderli.litool.core.text;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.lang.BeanPath;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * 一个简单的字符串替换工具
 * <p>
 * 支持通过自定义替换占位符,提供默认行为
 * 1. 通过数组参数替换, {@link #format(String, Object...)}, 占位符的值按顺序定位
 * 2. 通过beanPath替换,{@link #beanPath(String, Object)} ,占位符的值通过其名称获取
 * <pre>
 *
 *  replace("a={a},b={b}","1","2")
 *  replace("a={a},b={b},a={a}","1","2")
 *  replace("a={a},b={b}",{a=1,b=2})
 * </pre>
 * <p>
 * 支持转义字符 ` , 其仅在字面上上有效
 * <pre>
 *
 *  replace("a=`{{a}}","1","2") // a={1}
 *  replace("a={`{a}}","1","2") // a=1}
 * </pre>
 * <p>
 * 支持默认值行为 ,通过 :
 * <pre>
 *  replace("a={a:123}","1","2") // a={1}
 *  replace("a={a:123}") // a={123}
 * </pre>
 * <p>
 * 可以通过自定义函数，来对 : 后的进线特殊处理
 * <pre>
 * map.put("now", DateUtil.parse("20220101", "yyyyMMdd"));
 * parse("{now:yyyy-MM-dd}", (k, d) -> DateUtil.format(d, (Date) map.get(k)));
 * </pre>
 *
 * @author leaderli
 * @since 2022/8/14
 */
public class StrSubstitution {

    /**
     * 占位符开始标记
     */
    private static final String VARIABLE_BEING_CHAR = "{";
    /**
     * 占位符结束标记
     */
    private static final String VARIABLE_END_CHAR = "}";
    /**
     * 转义字符
     */
    private static final char ESCAPLE_CHAR = '`';
    /**
     * 解析开始标记
     */
    private static final int LITERAL = 1;
    private static final int VARIABLE_PREFIX = 2;
    private static final int VARIABLE_SUFFIX = 3;
    private static final int VARIABLE_LITERAL = 4;
    private static final int ESCAPE = 0;


    /**
     * 通过变量参数格式化文本。占位符在格式化文本中根据其出现位置计算索引,
     * 相同名称的占位符重用以前的索引。占位符将被对应索引的参数字符串值替换。
     * 如果索引超出提供的args范围,占位符将不被替换。
     * <p>
     * 例如:
     * <pre>
     *  format("a={a},b={b}","1") // "a=1,b={b}"
     *  format("a={a},b={b}","1","2") // "a=1,b=2"
     *  format("a={a},b={b},a={a}","1","2") // 变为 "a=1,b=2,a=1"
     * </pre>
     *
     * @param format 格式字符串
     * @param args   格式引用的参数
     * @return 格式化后的字符串
     * @see VariablesFunction
     * @see #parse(String, BiFunction)
     */
    public static String format(String format, Object... args) {

        return parse(format, new VariablesFunction(args));
    }

    /**
     * {@code  {xxx}} 被视为占位符 {@code  xxx}。不支持嵌套占位符。可以使用 {@link  #parse(String, String, String, BiFunction)}
     * 来自定义占位符前缀和后缀的方式来实现特殊字符的不被替换
     * <p>
     * 例如:
     * <pre>
     *     format("a={}")   // "a={}"
     * </pre>
     *
     * @param format          格式字符串
     * @param replaceFunction 接受占位符变量并返回替换值的函数
     * @return 格式化后的字符串
     */
    public static String parse(String format, BiFunction<String, String, Object> replaceFunction) {
        return parse(format, VARIABLE_BEING_CHAR, VARIABLE_END_CHAR, replaceFunction);
    }

    /**
     * 与{@link  #parse(String, BiFunction)}类似,不同之处在于占位符使用自定义字符定义。
     *
     * @param format          格式字符串
     * @param variablePrefix  占位符开始标记
     * @param variableSuffix  占位符结束标记
     * @param replaceFunction 接受占位符变量并返回替换值的函数
     * @return 格式化后的字符串
     */

    @SuppressWarnings("DuplicatedCode")
    public static String parse(String format, String variablePrefix, String variableSuffix, BiFunction<String, String, Object> replaceFunction) {
        if (format == null) {
            return "";
        }
        LiAssertUtil.assertTrue(!variablePrefix.isEmpty() && !variableSuffix.isEmpty());

        char[] prefixChars = variablePrefix.toCharArray();
        char[] suffixChars = variableSuffix.toCharArray();

        int state = LITERAL;

        StringBuilder result = new StringBuilder();

        StringBuilder prefixSB = new StringBuilder();
        StringBuilder literalSB = new StringBuilder();
        StringBuilder variableSB = new StringBuilder();
        StringBuilder suffixSB = new StringBuilder();

        for (char c : format.toCharArray()) {

            switch (state) {

                case LITERAL:
                    if (c == prefixChars[0]) {// 占位符开始
                        result.append(literalSB);
                        literalSB = new StringBuilder();
                        prefixSB.append(c);
                        state = VARIABLE_PREFIX;

                    } else if (c == ESCAPLE_CHAR) {
                        state = ESCAPE;
                    } else {
                        literalSB.append(c);
                    }
                    break;
                case ESCAPE:// 转义字符，仅支持在字面量中。
                    literalSB.append(c);
                    state = LITERAL;
                    break;
                case VARIABLE_PREFIX:

                    if (prefixChars.length > prefixSB.length()) {
                        if (c != prefixChars[prefixSB.length()]) {// 前缀不完全匹配，则视为字面量
                            result.append(prefixSB);
                            prefixSB = new StringBuilder();
                            if (c != prefixChars[0]) {// 不匹配的字节为不是前缀首字节
                                if (c == ESCAPLE_CHAR) {// 转义字节
                                    state = ESCAPE;
                                } else {
                                    literalSB.append(c);
                                    state = LITERAL;
                                }
                                break;
                            }
                        }
                        prefixSB.append(c);

                        if (prefixSB.length() == prefixChars.length) {// 前缀完全匹配了
                            state = VARIABLE_LITERAL;
                        }

                    } else {

                        // 前缀完全匹配后，首个字节为后缀首字节
                        if (c == suffixChars[0]) {
                            suffixSB.append(c);
                            state = VARIABLE_SUFFIX;
                        } else {
                            variableSB.append(c);
                            state = VARIABLE_LITERAL;
                        }
                    }
                    break;

                case VARIABLE_SUFFIX:
                    if (suffixChars.length > suffixSB.length()) {
                        if (c != suffixChars[suffixSB.length()]) {// 后缀不完全匹配，视为占位符变量
                            variableSB.append(suffixSB);
                            suffixSB = new StringBuilder();

                            if (c != suffixChars[0]) {// 不匹配的字节不为后缀首字节，视为占位符变量，否则视为后缀开始
                                variableSB.append(c);
                                state = VARIABLE_LITERAL;
                                break;
                            }
                        }
                        suffixSB.append(c);
                        if (suffixSB.length() == suffixChars.length) {// 后缀完全匹配，则替换变量，清空状态
                            result.append(replaceVariable(prefixSB, variableSB, suffixSB, replaceFunction));
                            prefixSB = new StringBuilder();
                            variableSB = new StringBuilder();
                            suffixSB = new StringBuilder();
                            state = LITERAL;
                        }
                    } else {


                        // 后缀完全匹配，则替换变量，清空状态
                        result.append(replaceVariable(prefixSB, variableSB, suffixSB, replaceFunction));
                        prefixSB = new StringBuilder();
                        variableSB = new StringBuilder();
                        suffixSB = new StringBuilder();

                        if (c == prefixChars[0]) {// 前缀开始
                            prefixSB.append(c);
                            state = VARIABLE_PREFIX;
                        } else {
                            if (c == ESCAPLE_CHAR) {
                                state = ESCAPE;
                            } else {
                                literalSB.append(c);
                                state = LITERAL;
                            }
                        }
                    }
                    break;

                case VARIABLE_LITERAL:

                    if (c == suffixChars[0]) {// 后缀开始
                        suffixSB.append(c);
                        state = VARIABLE_SUFFIX;
                    } else {
                        variableSB.append(c);
                    }
                    break;

                default:
                    break;

            }


        }

        // 后续状态处理
        if (state == VARIABLE_PREFIX) {
            result.append(prefixSB);
        } else if (state == VARIABLE_LITERAL) {// 占位符变量状态
            result.append(variablePrefix);
            result.append(variableSB);
        } else if (state == VARIABLE_SUFFIX) {
            if (suffixSB.length() == suffixChars.length) {// 占位符结尾
                result.append(replaceVariable(prefixSB, variableSB, suffixSB, replaceFunction));
            } else {
                result.append(variablePrefix);
                result.append(variableSB);
                result.append(suffixSB);
            }
        }

        return result.append(literalSB).toString();
    }

    private static Object replaceVariable(StringBuilder prefix, StringBuilder name, StringBuilder suffix, BiFunction<String, String, Object> replaceFunction) {
        String key;
        String def;
        int index = name.lastIndexOf(":");
        if (index > -1) {
            key = name.substring(0, index);
            def = name.substring(index + 1);
        } else {
            key = name.toString();
            def = prefix + key + suffix;
        }
        Object value = replaceFunction.apply(key, def);
        return key.isEmpty() || value == null ? def : value;
    }

    /**
     * 通过bean格式化文本。占位符在格式化文本中被视为bean路径表达式。
     * 并使用
     * {@link BeanPath#parse(Object, String)}搜索替换值。如果替换值
     * 为{@code null},占位符将不被替换。
     * 例如:
     * <pre>{@code
     * beanPath("a={a},b={b},c={c.a}",{a=1,b=2,c={a=1}}) // "a=1,b=2,c=1"
     * beanPath("a={a},b={b}",{a=1}) // "a=1,b={b}"
     * }</pre>
     *
     * @param format 格式字符串
     * @param bean   格式引用的参数
     * @return 格式化后的字符串
     * @see BeanPath#parse(Object)
     * @see #parse(String, BiFunction)
     */
    public static String beanPath(String format, Object bean) {
        return parse(format, (k, v) -> BeanPath.parse(bean, k).get(v));
    }

    /**
     * 与{@link #format(String, Object...)}相同,使用$作为占位符开始标记
     * <p>
     * 例如:
     * <pre>
     *  $format("a=${a},b=${b}","1") // "a=1,b={b}"
     *  $format("a=${a},b=${b}","1","2") // "a=1,b=2"
     *  $format("a=${a},b=${b},a=${a}","1","2") // 变为 "a=1,b=2,a=1"
     * </pre>
     *
     * @param format 格式字符串
     * @param args   格式引用的参数
     * @return 格式化后的字符串
     * @see VariablesFunction
     * @see #$parse(String, BiFunction)
     */
    public static String $format(String format, Object... args) {
        return $parse(format, new VariablesFunction(args));
    }

    /**
     * 与{@link #parse(String, BiFunction)}相同,使用$作为占位符开始标记
     * $}、${}不会被视为占位符,$是转义{的标记
     * 例如:
     * <pre>
     *     $format("a=${}")   // "a=${}"
     * </pre>
     *
     * @param format          格式字符串
     * @param replaceFunction 接受占位符变量并返回替换值的函数
     * @return 格式化后的字符串
     */
    public static String $parse(String format, BiFunction<String, String, Object> replaceFunction) {
        return parse(format, "${", "}", replaceFunction);
    }

    /**
     * 与{@link #beanPath(String, Object)} 相同,使用$作为占位符开始标记
     * <p>
     * eg:
     * <pre>{@code
     *
     * $beanPath("a=${a},b=${b},c=${c.a}",{a=1,b=2,c={a=1}}) // "a=1,b=2,c=1"
     * $beanPath("a=${a},b=${b}",${a=1}) // "a=1,b={b}"
     * }</pre>
     *
     * @param format 格式字符串
     * @param bean   格式引用的参数
     * @return 格式化后的字符串
     * @see #beanPath(String, Object)
     */
    public static String $beanPath(String format, Object bean) {
        return $parse(format, (k, v) -> BeanPath.parse(bean, k).get(v));
    }

    private static class VariablesFunction implements BiFunction<String, String, Object> {

        private final Object[] placeholderValues;
        private final List<String> placeholderNames = new ArrayList<>();
        /**
         * the current placeholder variable index, self-increment when there are new placeholder
         */
        private int index = 0;

        private VariablesFunction(Object[] placeholderValues) {
            this.placeholderValues = placeholderValues;
        }

        @Override
        public String apply(String key, String def) {

            int find = placeholderNames.indexOf(key);
            if (find > -1) {
                return String.valueOf(placeholderValues[find]);
            }
            if (this.index < placeholderValues.length) {
                placeholderNames.add(key);
                return String.valueOf(placeholderValues[this.index++]);
            }
            return def;
        }
    }


}
