package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.exception.LiAssertUtil;

import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/7/11
 */
public class BeanPath {
    // TODO

    private static final int BEGIN = 0;
    private static final int KEY = 1;
    private static final char ARR_BEGIN = '[';
    private static final char ARR_END = ']';
    private static final char VARIABLE_SPLIT = '.';

    public static BeanPath of(String expression) {

        Objects.requireNonNull(expression, " expression is null");


        int state = BEGIN;

        StringBuilder temp = new StringBuilder();
        for (char ch : expression.toCharArray()) {

            switch (state) {
                case BEGIN:

                    LiAssertUtil.assertFalse(ch == ARR_BEGIN || ch == ARR_END || ch == VARIABLE_SPLIT, String.format("state %d not support", state));

                    state = KEY;
                    temp.append(ch);

                    break;

                case KEY:

                default:
                    throw new UnsupportedOperationException(String.format("state %d not support", state));

            }

        }

        return new BeanPath();
    }
}
