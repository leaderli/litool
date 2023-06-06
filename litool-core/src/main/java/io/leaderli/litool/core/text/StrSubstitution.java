package io.leaderli.litool.core.text;

import io.leaderli.litool.core.lang.BeanPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 一个简单的字符串替换工具
 * <p>
 * 支持通过自定义替换占位符,提供默认行为
 * 1. 通过数组参数替换, {@link #format(String, Object...)}, 占位符的值按顺序定位
 * 2. 通过Map替换,{@link #forMap(String, Map)} ,占位符的值通过其名称获取
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

    public static final char VARIABLE_BEING_CHAR = '{';
    public static final char VARIABLE_END_CHAR = '}';
    public static final int START = 0;
    private static final int LITERAL = 1;
    private static final int VARIABLE_BEGIN = 2;
    private static final int VARIABLE_LITERAL = 3;

    /**
     * 通过Map格式化文本。占位符在格式化文本中被视为Map键。
     * 并使用{@link BeanPath#parse(Object, String)}获取替换值。如果替换值
     * 为{@code null},占位符将不被替换。
     * 例如:
     * <pre>{@code
     * beanPath("a={a},b={b},c={c.a}",{a=1,b=2,c={a=1}}) // "a=1,b=2,c=1"
     * beanPath("a={a},b={b}",{a=1}) // "a=1,b={b}"
     * }</pre>
     *
     * @param format 格式字符串
     * @param map    Map
     * @return 格式化后的字符串
     * @see #format(String, Function)
     */
    public static String forMap(String format, Map<String, ?> map) {
        return format(format, map::get);
    }

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

    /**
     * 与{@link  #format(String, Function)}类似,不同之处在于占位符使用自定义字符定义。
     *
     * @param format          格式字符串
     * @param variableBegin   占位符开始标记
     * @param variableEnd     占位符结束标记
     * @param replaceFunction 接受占位符变量并返回替换值的函数
     * @return 格式化后的字符串
     */

    public static String parse(String format, char variableBegin, char variableEnd, Function<String, Object> replaceFunction) {
        if (format == null) {
            return "";
        }

        int state = START;

        StringBuilder result = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        //  state machine
        for (char c : format.toCharArray()) {

            switch (state) {
                case START:

                    if (c == variableBegin) {

                        state = VARIABLE_BEGIN;
                    } else {
                        temp.append(c);
                        state = LITERAL;
                    }
                    break;
                case LITERAL:
                    if (c == variableBegin) {
                        state = VARIABLE_BEGIN;
                        result.append(temp);
                        temp = new StringBuilder();

                    } else {
                        temp.append(c);
                    }
                    break;
                case VARIABLE_BEGIN:
                    if (c == variableBegin) {
                        result.append(variableBegin);
                        state = START;
                    } else if (c == variableEnd) {
                        result.append(variableBegin).append(variableEnd);
                        state = START;
                    } else {
                        temp.append(c);
                        state = VARIABLE_LITERAL;
                    }
                    break;

                case VARIABLE_LITERAL:

                    if (c == variableEnd) {
                        String key = temp.toString();
                        Object value = replaceFunction.apply(key);
                        result.append(value == null ? variableBegin + key + variableEnd : value);
                        temp = new StringBuilder();
                        state = START;
                    } else {
                        temp.append(c);
                    }
                    break;

                default:
                    break;

            }


        }
        if (state == VARIABLE_BEGIN || state == VARIABLE_LITERAL) {
            result.append(variableBegin);
        }
        return result.append(temp).toString();
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
        return parse(format, '$', '}', name -> {
            if (name.startsWith("{")) {
                if (name.endsWith("{")) {
                    return "${}";
                }
                return replaceFunction.apply(name.substring(1));
            }
            return name;
        });
    }

    /**
     * 与{@link #forMap(String, Map)} 相同,使用$作为占位符开始标记
     * $}、${}不会被视为占位符,$是转义{的标记     * <p>
     * eg:
     * <pre>{@code
     * beanPath("a=${a},b=${b},c=${c.a}",{a=1,b=2,c={a=1}}) // "a=1,b=2,c=1"
     * beanPath("a=${a},b=${b}",${a=1}) // "a=1,b={b}"
     * }</pre>
     *
     * @param format 格式字符串
     * @param map    Map
     * @return 格式化后的字符串
     * @see #$format(String, Function)
     */
    public static String $forMap(String format, Map<String, ?> map) {
        return $format(format, map::get);
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
