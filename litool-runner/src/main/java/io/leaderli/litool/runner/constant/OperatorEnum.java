package io.leaderli.litool.runner.constant;

import io.leaderli.litool.core.util.ObjectsUtil;

import java.util.Arrays;
import java.util.List;

public enum OperatorEnum {

    GREATER_THAN(">", "gt", "大于") {
        @Override
        public <T> boolean apply(int compare) {
            return compare > 0;
        }

    },
    GREATER_THAN_OR_EQUALS(">=", "ge", "大于等于") {
        @Override
        public <T> boolean apply(int compare) {
            return compare >= 0;
        }
    },
    EQUALS("=", "==", "equals", "等于") {
        @Override
        public <T> boolean apply(int compare) {
            return compare == 0;
        }
    },
    LESS_THAN("<", "lt", "小于") {
        @Override
        public <T> boolean apply(int compare) {
            return compare < 0;
        }
    },
    LESS_THAN_OR_EQUALS("<=", "le", "小于等于") {
        @Override
        public <T> boolean apply(int compare) {
            return compare <= 0;
        }
    };

    OperatorEnum(String... strings) {
        this.strings = Arrays.asList(strings);
    }

    public final List<String> strings;

    public static OperatorEnum get(String op) {
        for (OperatorEnum operatorEnum : values()) {
            if (operatorEnum.strings.contains(op)) {
                return operatorEnum;
            }
        }
        throw new UnsupportedOperationException(String.format("OperatorEnum unsupported [%s]", op));
    }

    public abstract <T> boolean apply(int compare);

    public final <T> boolean apply(T left, T right) {

        int compare = ObjectsUtil.compare(left, right);

        return apply(compare);

    }
}
