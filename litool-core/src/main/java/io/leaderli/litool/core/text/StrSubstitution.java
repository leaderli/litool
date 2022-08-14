package io.leaderli.litool.core.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author leaderli
 * @since 2022/8/14
 */
public class StrSubstitution {

    public static final char BEING = '{';
    public static final char END = '}';
    public static final int START = 0;
    private static final int LITERAL = 1;
    private static final int VARIABLE_BEGIN = 2;
    private static final int VARIABLE_LITERAL = 3;
    private static final int VARIABLE_END = 4;


    public static String replace(String text, Object... args) {
        return replace(text, new VariablesFunction(args));
    }

    public static String replace(String text, Map<String, Object> map) {
        return replace(text, s -> map.getOrDefault(s, "").toString());
    }

    public static String replace(String text, Function<String, String> convert) {

        int state = START;

        List<SubstitutionModel> substitutionModelList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {

            switch (state) {
                case START:

                    if (c == BEING) {

                        state = VARIABLE_BEGIN;
                    } else {
                        sb.append(c);
                        state = LITERAL;
                    }
                    break;
                case LITERAL:
                    if (c == BEING) {
                        state = VARIABLE_BEGIN;
                        substitutionModelList.add(new LiteralSubstitution(sb.toString()));
                        sb = new StringBuilder();

                    } else {
                        sb.append(c);
                    }
                    break;
                case VARIABLE_BEGIN:
                    if (c == BEING) {
                        substitutionModelList.add(new LiteralSubstitution("{"));
                        state = START;
                    } else if (c == END) {
                        substitutionModelList.add(new LiteralSubstitution("{}"));
                        state = START;
                    } else {
                        sb.append(c);
                        state = VARIABLE_LITERAL;
                    }
                    break;

                case VARIABLE_LITERAL:

                    if (c == END) {
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
        return substitutionModelList.stream().map(Supplier::get).collect(Collectors.joining());
    }


    private static class VariablesFunction implements Function<String, String> {

        private final Object[] args;
        private final List<String> keys = new ArrayList<>();
        private int length = 0;

        private VariablesFunction(Object[] args) {
            this.args = args;
        }

        @Override
        public String apply(String s) {

            int index = keys.indexOf(s);
            if (index > -1) {
                return args[index] + "";
            }
            if (this.length < args.length) {
                keys.add(s);
                return args[this.length++] + "";
            }
            return "";
        }
    }

    private static abstract class SubstitutionModel implements Supplier<String> {

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
