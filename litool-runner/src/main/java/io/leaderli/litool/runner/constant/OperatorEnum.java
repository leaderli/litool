package io.leaderli.litool.runner.constant;

import io.leaderli.litool.core.util.ObjectsUtil;

public enum OperatorEnum {

    GREATER_THAN(">", "gt", "大于") {
        @Override
        public boolean apply(int compare) {
            return compare > 0;
        }

    },
    GREATER_THAN_OR_EQUALS(">=", "ge", "大于等于") {
        @Override
        public boolean apply(int compare) {
            return compare >= 0;
        }
    },
    EQUALS("=", "==", "equals", "等于") {
        @Override
        public boolean apply(int compare) {
            return compare == 0;
        }
    },
    LESS_THAN("<", "lt", "小于") {
        @Override
        public boolean apply(int compare) {
            return compare < 0;
        }
    },
    LESS_THAN_OR_EQUALS("<=", "le", "小于等于") {
        @Override
        public boolean apply(int compare) {
            return compare <= 0;
        }
    };

    private final String[] operations;

    OperatorEnum(String... operations) {
        this.operations = operations;
    }

    public static OperatorEnum get(String op) {

        for (OperatorEnum operatorEnum : values()) {
            for (String operation : operatorEnum.operations) {
                if (operation.equals(op)) {
                    return operatorEnum;
                }
            }
        }
        throw new UnsupportedOperationException(String.format("OperatorEnum unsupported [%s]", op));
    }

    public final <T> boolean apply(T left, T right) {

        int compare = ObjectsUtil.compare(left, right);

        return apply(compare);

    }

    public abstract boolean apply(int compare);
}
