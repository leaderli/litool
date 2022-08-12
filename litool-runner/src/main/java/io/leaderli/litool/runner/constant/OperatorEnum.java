package io.leaderli.litool.runner.constant;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum OperatorEnum {

    GREATER_THAN(">", "gt", "大于") {
        @Override
        public boolean compare(Integer left, Integer right) {
            return left.compareTo(right) > 0;
        }

        @Override
        public boolean compare(LocalDate left, LocalDate right) {
            return left.compareTo(right) > 0;
        }

        @Override
        public boolean compare(LocalTime left, LocalTime right) {
            return left.compareTo(right) > 0;
        }
    },
    GREATER_THAN_OR_EQUALS(">=", "ge", "大于等于") {
        @Override
        public boolean compare(Integer left, Integer right) {
            return left.compareTo(right) >= 0;
        }

        @Override
        public boolean compare(LocalDate left, LocalDate right) {
            return left.compareTo(right) >= 0;
        }

        @Override
        public boolean compare(LocalTime left, LocalTime right) {
            return left.compareTo(right) >= 0;
        }
    },
    EQUALS("=", "==", "equals", "等于") {
        @Override
        public boolean compare(Integer left, Integer right) {
            return left.compareTo(right) == 0;
        }

        @Override
        public boolean compare(LocalDate left, LocalDate right) {
            return left.compareTo(right) == 0;
        }

        @Override
        public boolean compare(LocalTime left, LocalTime right) {
            return left.compareTo(right) == 0;
        }
    },
    LESS_THAN("<", "lt", "小于") {
        @Override
        public boolean compare(Integer left, Integer right) {
            return left.compareTo(right) < 0;
        }

        @Override
        public boolean compare(LocalDate left, LocalDate right) {
            return left.compareTo(right) < 0;
        }

        @Override
        public boolean compare(LocalTime left, LocalTime right) {
            return left.compareTo(right) < 0;
        }
    },
    LESS_THAN_OR_EQUALS("<=", "le", "小于等于") {
        @Override
        public boolean compare(Integer left, Integer right) {
            return left.compareTo(right) <= 0;
        }

        @Override
        public boolean compare(LocalDate left, LocalDate right) {
            return left.compareTo(right) <= 0;
        }

        @Override
        public boolean compare(LocalTime left, LocalTime right) {
            return left.compareTo(right) <= 0;
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

    public abstract boolean compare(Integer left, Integer right);

    public abstract boolean compare(LocalDate left, LocalDate right);

    public abstract boolean compare(LocalTime left, LocalTime right);

}
