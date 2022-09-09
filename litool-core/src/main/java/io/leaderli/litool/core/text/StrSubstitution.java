package io.leaderli.litool.core.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
     * Returns a formatted string using the specified format string and arguments.
     * <pre>
     *  replace("a={a},b={b}","1","2")
     *  replace("a={a},b={b},a={a}","1","2")
     * </pre>
     *
     * @param format a format string
     * @param args   Arguments referenced by the format, If there are more arguments than format
     *               specifiers, the extra arguments are ignored. If there are less arguments than
     *               format specifiers, will use the '{key}'
     * @return a formatted string
     */
    public static String format(String format, Object... args) {
        return format(format, new VariablesFunction(args));
    }

    public static String format(String text, Function<String, String> convert) {

        List<SubstitutionModel> substitutionModelList = parse(text, convert);
        return substitutionModelList.stream().map(Supplier::get).collect(Collectors.joining());
    }

    /**
     * @param format  a format string
     * @param convert a function that accept the placeholder variable and return a value to replace it
     * @return the  list of chunk string split by variable and literal
     */
    private static List<SubstitutionModel> parse(String format, Function<String, String> convert) {
        if (format == null) {
            return new ArrayList<>();
        }

        int state = START;

        List<SubstitutionModel> substitutionModelList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        // 解析状态机
        for (char c : format.toCharArray()) {

            switch (state) {
                case START:

                    if (c == VARIABLE_BEING_CHAR) {

                        state = VARIABLE_BEGIN;
                    } else {
                        sb.append(c);
                        state = LITERAL;
                    }
                    break;
                case LITERAL:
                    if (c == VARIABLE_BEING_CHAR) {
                        state = VARIABLE_BEGIN;
                        substitutionModelList.add(new LiteralSubstitution(sb.toString()));
                        sb = new StringBuilder();

                    } else {
                        sb.append(c);
                    }
                    break;
                case VARIABLE_BEGIN:
                    if (c == VARIABLE_BEING_CHAR) {
                        substitutionModelList.add(new LiteralSubstitution("{"));
                        state = START;
                    } else if (c == VARIABLE_END_CHAR) {
                        substitutionModelList.add(new LiteralSubstitution("{}"));
                        state = START;
                    } else {
                        sb.append(c);
                        state = VARIABLE_LITERAL;
                    }
                    break;

                case VARIABLE_LITERAL:

                    if (c == VARIABLE_END_CHAR) {
                        substitutionModelList.add(new VariableSubstitution(sb.toString(), convert));
                        sb = new StringBuilder();
                        state = START;
                    } else {
                        sb.append(c);
                    }
                    break;

                default:
                    break;

            }


        }
        if (sb.length() > 0) {
            substitutionModelList.add(new LiteralSubstitution(sb.toString()));
        }
        return substitutionModelList;
    }

    /**
     * Returns a formatted string using the specified format string and map.
     * <pre>
     * replace("a={a},b={b}",{a=1,b=2})
     * </pre>
     *
     * @param format a format string
     * @param map    Arguments referenced by the format, get argument by it's placeholder name, it not find  will use
     *               the ''
     * @return a formatted string
     */
    public static String format(String format, Map<String, Object> map) {
        return format(format, s -> map.getOrDefault(s, "").toString());
    }

    private static class VariablesFunction implements Function<String, String> {

        private final Object[] placeholderValues;
        private final List<String> placeholderNames = new ArrayList<>();
        /**
         * 当前填充的角标位置，当遇到新变量时，角标位置+1
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
            return "{" + s + "}";
        }
    }

    private abstract static class SubstitutionModel implements Supplier<String> {

        public final String value;

        private SubstitutionModel(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }


    private static class VariableSubstitution extends SubstitutionModel {
        private final Function<String, String> convert;

        private VariableSubstitution(String value, Function<String, String> convert) {
            super(value);
            this.convert = convert;
        }

        @Override
        public String get() {
            return convert.apply(value);
        }
    }

    private static class LiteralSubstitution extends SubstitutionModel {
        private LiteralSubstitution(String value) {
            super(value);
        }

        @Override
        public String get() {
            return value;
        }
    }
}
