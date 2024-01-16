package io.leaderli.litool.core.text;

import io.leaderli.litool.core.exception.LiAssertUtil;

public class StringPlaceholder {
    private static final int STATE_ESCAPE = 0;
    private static final int STATE_LITERAL = 1;
    private static final int STATE_VARIABLE_BEGIN = 2;
    private static final int STATE_VARIABLE_END = 3;
    private static final int STATE_VARIABLE = 4;

    public final char[] variable_prefix;
    public final char[] variable_suffix;
    public final char escap_char;


    private StringPlaceholder(Builder builder) {
        this.variable_prefix = builder.variable_prefix;
        this.variable_suffix = builder.variable_suffix;
        this.escap_char = builder.escap;

    }

    public void parse(String str, PlaceholderFunction place) {
        if (StringUtils.isEmpty(str)) {
            return;
        }

        StringBuilder literal = new StringBuilder();
        StringBuilder prefix = new StringBuilder();
        StringBuilder variable = new StringBuilder();

        StringBuilder suffix = new StringBuilder();
        int state = STATE_LITERAL;


        for (char c : str.toCharArray()) {

            switch (state) {
                case STATE_LITERAL:
                    if (c == variable_prefix[0]) {// 占位符开始

                        prefix.append(c);
                        state = STATE_VARIABLE_BEGIN;

                    } else if (escap_char != 0 && c == escap_char) {
                        state = STATE_ESCAPE;
                    } else {
                        literal.append(c);
                    }
                    break;
                case STATE_ESCAPE:// 转义字符，仅支持在字面量中。
                    literal.append(c);
                    state = STATE_LITERAL;
                    break;

                case STATE_VARIABLE_BEGIN:

                    if (variable_prefix.length > prefix.length()) {
                        if (c != variable_prefix[prefix.length()]) {// 前缀不完全匹配，则视为字面量
                            literal.append(prefix);
                            prefix = new StringBuilder();
                            if (c != variable_prefix[0]) {// 不匹配的字节为不是前缀首字节
                                if (escap_char != 0 && c == escap_char) {// 转义字节
                                    state = STATE_ESCAPE;
                                } else {
                                    literal.append(c);
                                    state = STATE_LITERAL;
                                }
                                break;
                            }
                        }
                        prefix.append(c);

                        if (prefix.length() == variable_prefix.length) {// 前缀完全匹配了
                            state = STATE_VARIABLE;
                            place.literal(literal);
                            literal = new StringBuilder();
                        }

                    } else {
                        place.literal(literal);
                        literal = new StringBuilder();
                        // 前缀完全匹配后，首个字节为后缀首字节
                        if (c == variable_suffix[0]) {
                            suffix.append(c);
                            state = STATE_VARIABLE_END;
                        } else {
                            variable.append(c);
                            state = STATE_VARIABLE;
                        }
                    }
                    break;

                case STATE_VARIABLE:

                    if (c == variable_suffix[0]) {// 后缀开始
                        suffix.append(c);
                        state = STATE_VARIABLE_END;
                    } else {
                        variable.append(c);
                    }
                    break;
                case STATE_VARIABLE_END:
                    if (variable_suffix.length > suffix.length()) {
                        if (c != variable_suffix[suffix.length()]) {// 后缀不完全匹配，视为占位符变量
                            variable.append(suffix);
                            suffix = new StringBuilder();

                            if (c != variable_suffix[0]) {// 不匹配的字节不为后缀首字节，视为占位符变量，否则视为后缀开始
                                variable.append(c);
                                state = STATE_VARIABLE;
                                break;
                            }
                        }
                        suffix.append(c);
                        if (suffix.length() == variable_suffix.length) {// 后缀完全匹配
                            place.variable(variable);
                            prefix = new StringBuilder();
                            variable = new StringBuilder();
                            suffix = new StringBuilder();
                            state = STATE_LITERAL;
                        }
                    } else {


                        // 后缀完全匹配，则替换变量，清空状态
                        place.variable(variable);
                        prefix = new StringBuilder();
                        variable = new StringBuilder();
                        suffix = new StringBuilder();

                        if (c == variable_prefix[0]) {// 前缀开始
                            prefix.append(c);
                            state = STATE_VARIABLE_BEGIN;
                        } else {
                            if (escap_char != 0 && c == escap_char) {
                                state = STATE_ESCAPE;
                            } else {
                                literal.append(c);
                                state = STATE_LITERAL;
                            }
                        }
                    }
                    break;

                default:
                    break;
            }


        }
        // 后续状态处理
        if (state == STATE_LITERAL) {
            place.literal(literal);
        } else if (state == STATE_VARIABLE_BEGIN) {
            place.literal(literal.append(prefix));
        } else if (state == STATE_VARIABLE) {// 占位符变量状态
            place.literal(prefix.append(variable));
        } else if (state == STATE_VARIABLE_END) {
            if (suffix.length() == variable_suffix.length) {// 占位符结尾
                place.variable(variable);
            } else {
                place.literal(prefix.append(variable).append(suffix));
            }
        }
    }

    public static class Builder {
        private char[] variable_prefix = {'{'};

        private char[] variable_suffix = {'}'};
        private char escap = 0;


        public Builder variable_prefix(String variable_prefix) {
            LiAssertUtil.assertFalse(variable_prefix.isEmpty());
            this.variable_prefix = variable_prefix.toCharArray();
            return this;
        }

        public Builder variable_suffix(String variable_suffix) {
            LiAssertUtil.assertFalse(variable_suffix.isEmpty());
            this.variable_suffix = variable_suffix.toCharArray();
            return this;
        }

        public Builder escap(char escap) {
            this.escap = escap;
            return this;
        }


        public StringPlaceholder build() {
            return new StringPlaceholder(this);
        }
    }


}
