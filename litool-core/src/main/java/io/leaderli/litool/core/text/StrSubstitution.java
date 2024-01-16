package io.leaderli.litool.core.text;

import io.leaderli.litool.core.lang.BeanPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * 一个简单的字符串替换工具
 * <p>
 * 支持通过自定义替换占位符,提供默认行为
 * 1. 通过数组参数替换, {@link #format(String, Object...)}, 占位符的值按顺序定位
 * 2. 通过beanPath替换,{@link #beanPath(String, Object)} ,占位符的值通过其名称获取
 * 3. 通过beanPath替换,{@link #map(String, Map)} ,占位符的值通过其名称获取
 * <pre>
 *
 *  replace("a={a},b={b}","1","2")
 *  replace("a={a},b={b},a={a}","1","2")
 *  replace("a={a},b={b}",{a=1,b=2})
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
 * <p>
 * 不支持嵌套占位符
 *
 * @author leaderli
 * @since 2022/8/14
 */
public class StrSubstitution {


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
     * @see #parse(String, String, String, BiFunction)
     */
    public static String format(String format, Object... args) {

        return parse(format, "{", "}", new VariablesFunction(args));
    }

    public static String map(String format, Map<String, Object> map) {
        return parse(format, "{", "}", map::getOrDefault);
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
     * @see #parse(String, String, String, BiFunction)
     */
    public static String beanPath(String format, Object bean) {
        return parse(format, "{", "}", (k, v) -> BeanPath.parse(bean, k).get(v));
    }


    public static String $format(String format, Object... args) {
        return parse(format, "${", "}", new VariablesFunction(args));
    }


    public static String $beanPath(String format, Object bean) {
        return parse(format, "${", "}", (k, v) -> BeanPath.parse(bean, k).get(v));

    }

    private static Object replaceVariable(String prefix, StringBuilder name, String suffix, BiFunction<String, String, Object> replaceFunction) {
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
     * @param str             格式字符串
     * @param variablePrefix  占位符开始标记
     * @param variableSuffix  占位符结束标记
     * @param replaceFunction 接受占位符变量并返回替换值的函数
     * @return 格式化后的字符串
     * @see StringPlaceholder#parse(String, PlaceholderFunction)
     * @see #replaceVariable(String, StringBuilder, String, BiFunction)
     */

    public static String parse(String str, String variablePrefix, String variableSuffix, BiFunction<String, String, Object> replaceFunction) {

        StringBuilderPlaceholderFunction placer = new StringBuilderPlaceholderFunction() {
            @Override
            public void variable(StringBuilder variable) {
                append(replaceVariable(variablePrefix, variable, variableSuffix, replaceFunction));
            }
        };
        new StringPlaceholder.Builder()
                .variable_prefix(variablePrefix)
                .variable_suffix(variableSuffix)
                .build()
                .parse(str, placer);

        return placer.toString();
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
