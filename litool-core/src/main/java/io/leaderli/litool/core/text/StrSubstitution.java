package io.leaderli.litool.core.text;

import io.leaderli.litool.core.lang.BeanPath;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * a simple str substitution tool
 * <p>
 * support replace placeholder by custom, provide to default behavior
 * 1. replace by array parameters , {@link #format(String, Object...)}, the placeholder value is positioned in order
 * 2. replace by map , {@link #format(String, Object...)}, the placeholder value  get by it's name
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
     * format text by variable parameter. the placeholder in format text calculates it's index based on
     * where it's appear, the same name placeholder reuse previous index. the placeholder will replaced
     * the the index parameter string value. if index is outbound of provide args, the placeholder will
     * not be replaced.
     * <p>
     * eg:
     * <pre>
     *  format("a={a},b={b}","1") // "a=1,b={b}"
     *  format("a={a},b={b}","1","2") // "a=1,b=2"
     *  format("a={a},b={b},a={a}","1","2") // to "a=1,b=2,a=1"
     * </pre>
     *
     * @param format a format string
     * @param args   Arguments referenced by the format
     * @return a formatted string
     * @see VariablesFunction
     * @see #format(String, Function)
     */
    public static String format(String format, Object... args) {

        return format(format, new VariablesFunction(args));
    }

    /**
     * format text by bean. the placeholder in format text are regard as a bean-path expression.
     * and use {@link BeanPath#parse(Object, String)} to search the replace value. if replace value
     * is {@code  null} the placeholder will not be replaced
     * eg:
     * <pre>{@code
     * beanPath("a={a},b={b},c={c.a}",{a=1,b=2,c={a=1}}) // "a=1,b=2,c=1"
     * beanPath("a={a},b={b}",{a=1}) // "a=1,b={b}"
     * }</pre>
     *
     * @param format a format string
     * @param bean   Arguments referenced by the format
     * @return a formatted string
     * @see BeanPath#parse(Object)
     * @see #format(String, Function)
     */
    public static String beanPath(String format, Object bean) {
        return format(format, s -> BeanPath.parse(bean, s).get());
    }

    /**
     * the {@code  {xxx}}  are regard as placeholder {@code  xxx}. not support recursive placeholder.
     * the {} will not regard as placeholder, {{ will regard as escape {
     * <p>
     * eg:
     *
     * <pre>{@code
     *     format("a={}")   // "a={}"
     *     format("a={{a}") // "a={a}"
     * }</pre>
     *
     * @param format          a format string
     * @param replaceFunction a function that accept the placeholder variable and return a value to replace it
     * @return a formatted string
     */
    public static String format(String format, Function<String, Object> replaceFunction) {
        return parse(format, VARIABLE_BEING_CHAR, VARIABLE_END_CHAR, replaceFunction);
    }

    /**
     * just like {@link  #format(String, Function)}, the different is placeholder define with custom char
     *
     * @param format          a format string
     * @param variableBegin   the placeholder begin mark
     * @param variableEnd     the placeholder end mark
     * @param replaceFunction a function that accept the placeholder variable and return a value to replace it
     * @return a formatted string
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
     * just same as {@link  #beanPath(String, Object)}, use ${ as begin placeholder
     * <p>
     * eg:
     * <pre>{@code
     *
     * $beanPath("a=${a},b=${b},c=${c.a}",{a=1,b=2,c={a=1}}) // "a=1,b=2,c=1"
     * $beanPath("a=${a},b=${b}",${a=1}) // "a=1,b={b}"
     * }</pre>
     *
     * @param format a format string
     * @param bean   Arguments referenced by the format
     * @return a formatted string
     */
    public static String $beanPath(String format, Object bean) {
        return $format(format, s -> BeanPath.parse(bean, s).get());
    }

    /**
     * just same as {@link  #format(String, Function)}, use ${ as begin placeholder
     * the $},${} will not regard as placeholder, $$ will regard as escape {
     * eg:
     *
     * <pre>{@code
     *     $format("a=${}")   // "a=${}"
     *     $format("a=$${a}") // "a=${a}"
     * }</pre>*
     *
     * @param format          a format string
     * @param replaceFunction a function that accept the placeholder variable and return a value to replace it
     * @return a formatted string
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
                return placeholderValues[find] + "";
            }
            if (this.index < placeholderValues.length) {
                placeholderNames.add(s);
                return placeholderValues[this.index++] + "";
            }
            return null;
        }
    }


}
