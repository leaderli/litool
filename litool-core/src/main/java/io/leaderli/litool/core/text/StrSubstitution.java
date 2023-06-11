package io.leaderli.litool.core.text;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.lang.BeanPath;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
     * 解析开始标记
     */
    private static final int LITERAL = 1;
    private static final int VARIABLE_PREFIX = 2;
    private static final int VARIABLE_SUFFIX = 4;
    private static final int VARIABLE_LITERAL = 6;


    /**
     * {@code  {xxx}} 被视为占位符 {@code  xxx}。不支持嵌套占位符。
     * {}不会被视为占位符,{{}会被视为转义{
     * <p>
     * 例如:
     * <pre>
     *     format("a={}")   // "a={}"
     *     format("a={{a}") // "a={a}"
     * </pre>
     *
     * @param format          格式字符串
     * @param replaceFunction 接受占位符变量并返回替换值的函数
     * @return 格式化后的字符串
     */
    public static String format(String format, Function<String, Object> replaceFunction) {
        return parse(format, VARIABLE_BEING_CHAR, VARIABLE_END_CHAR, replaceFunction);
    }

    private static void replaceVariable(StringBuilder prefix, StringBuilder variableStr, StringBuilder suffix, Function<String, Object> replaceFunction, StringBuilder result) {
        String key = variableStr.toString();
        Object value = replaceFunction.apply(key);
        result.append(key.length() == 0 || value == null ? prefix + key + suffix : value);
    }

    /**
     * 与{@link #format(String, Function)}相同,使用$作为占位符开始标记
     * $}、${}不会被视为占位符,$是转义{的标记
     * 例如:
     * <pre>
     *     $format("a=${}")   // "a=${}"
     *     $format("a=$${a}") // "a=${a}"
     * </pre>
     *
     * @param format          格式字符串
     * @param replaceFunction 接受占位符变量并返回替换值的函数
     * @return 格式化后的字符串
     */
    public static String $format(String format, Function<String, Object> replaceFunction) {

        return parse(format, "${", "}", replaceFunction);
//        return parse(format, "$", "}", name -> {
//            if (name.startsWith("{")) {
//                if (name.endsWith("{")) {
//                    return "${}";
//                }
//                return replaceFunction.apply(name.substring(1));
//            }
//            return name;
//        });
    }

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
     * @see #format(String, Function)
     */
    public static String format(String format, Object... args) {

        return format(format, new VariablesFunction(args));
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
     * @see #format(String, Function)
     */
    public static String beanPath(String format, Object bean) {
        return format(format, s -> BeanPath.parse(bean, s).get());
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
     * @see #$format(String, Function)
     */
    public static String $format(String format, Object... args) {

        return $format(format, new VariablesFunction(args));
    }

    /**
     * 与{@link  #format(String, Function)}类似,不同之处在于占位符使用自定义字符定义。
     *
     * @param format          格式字符串
     * @param variablePrefix  占位符开始标记
     * @param variableSuffix  占位符结束标记
     * @param replaceFunction 接受占位符变量并返回替换值的函数
     * @return 格式化后的字符串
     */

    @SuppressWarnings("DuplicatedCode")
    public static String parse(String format, String variablePrefix, String variableSuffix, Function<String, Object> replaceFunction) {
        if (format == null) {
            return "";
        }
        LiAssertUtil.assertTrue(variablePrefix.length() > 0 && variableSuffix.length() > 0);

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
                    if (c == prefixChars[0]) {
                        result.append(literalSB);
                        literalSB = new StringBuilder();
                        prefixSB.append(c);
                        state = VARIABLE_PREFIX;

                    } else {
                        literalSB.append(c);
                    }
                    break;
                case VARIABLE_PREFIX:

                    if (prefixChars.length > prefixSB.length()) {
                        if (c != prefixChars[prefixSB.length()]) {// 前缀剩余字符不匹配,将其全部填充
                            result.append(prefixSB);
                            prefixSB = new StringBuilder();
                            if (c != prefixChars[0]) {
                                literalSB.append(c);
                                state = LITERAL;
                                break;
                            }
                        }
                        prefixSB.append(c);

                        if (prefixSB.length() == prefixChars.length) {// 前缀完全匹配了
                            state = VARIABLE_LITERAL;
                        }

                    } else {

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
                        if (c != suffixChars[suffixSB.length()]) {// 后缀剩余字符不匹配,将其全部填充
                            variableSB.append(suffixSB);
                            suffixSB = new StringBuilder();

                            if (c != suffixChars[0]) {
                                variableSB.append(c);
                                state = VARIABLE_LITERAL;
                                break;
                            }
                        }
                        suffixSB.append(c);
                        if (suffixSB.length() == suffixChars.length) {// 后缀完全匹配了
                            String key = variableSB.toString();
                            Object value = replaceFunction.apply(key);
                            result.append(key.length() == 0 || value == null ? prefixSB + key + suffixSB : value);
                            prefixSB = new StringBuilder();
                            variableSB = new StringBuilder();
                            suffixSB = new StringBuilder();
                            state = LITERAL;
                        }
                    } else {


                        String key = variableSB.toString();
                        Object value = replaceFunction.apply(key);
                        result.append(key.length() == 0 || value == null ? prefixSB + key + suffixSB : value);
                        prefixSB = new StringBuilder();
                        variableSB = new StringBuilder();
                        suffixSB = new StringBuilder();

                        if (c == prefixChars[0]) {
                            prefixSB.append(c);
                            state = VARIABLE_PREFIX;
                        } else {
                            literalSB.append(c);
                            state = LITERAL;
                        }
                    }
                    break;

                case VARIABLE_LITERAL:

                    if (c == suffixChars[0]) {
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

        if (state == VARIABLE_PREFIX) {
            result.append(prefixSB);
        } else if (state == VARIABLE_LITERAL) {
            result.append(variablePrefix);
            result.append(variableSB);
        } else if (state == VARIABLE_SUFFIX) {
            if (suffixSB.length() == suffixChars.length) {
                String key = variableSB.toString();
                Object value = replaceFunction.apply(key);
                result.append(key.length() == 0 || value == null ? prefixSB + key + suffixSB : value);

            } else {
                result.append(variablePrefix);
                result.append(variableSB);
                result.append(suffixSB);
            }
        }

        return result.append(literalSB).toString();
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
        return $format(format, s -> BeanPath.parse(bean, s).get());
    }

    private static class VariablesFunction implements Function<String, Object> {

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
        public String apply(String s) {

            int find = placeholderNames.indexOf(s);
            if (find > -1) {
                return String.valueOf(placeholderValues[find]);
            }
            if (this.index < placeholderValues.length) {
                placeholderNames.add(s);
                return String.valueOf(placeholderValues[this.index++]);
            }
            return null;
        }
    }


}
