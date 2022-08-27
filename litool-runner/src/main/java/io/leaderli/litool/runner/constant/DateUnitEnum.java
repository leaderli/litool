package io.leaderli.litool.runner.constant;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public enum DateUnitEnum {

    YEAR("y") {
        @Override
        public LocalDate plus(LocalDate date, Integer gap) {
            return date.plusYears(gap);
        }

        @Override
        public LocalDate minus(LocalDate date, Integer gap) {
            return date.minusYears(gap);
        }

        @Override
        public Integer get(LocalDate date) {
            return date.getYear();
        }
    },
    MONTH("m") {
        @Override
        public LocalDate plus(LocalDate date, Integer gap) {
            return date.plusMonths(gap);
        }

        @Override
        public LocalDate minus(LocalDate date, Integer gap) {
            return date.minusMonths(gap);
        }

        @Override
        public Integer get(LocalDate date) {
            return date.getMonthValue();
        }
    },
    DAY("d") {
        @Override
        public LocalDate plus(LocalDate date, Integer gap) {
            return date.plusDays(gap);
        }

        @Override
        public LocalDate minus(LocalDate date, Integer gap) {
            return date.minusDays(gap);
        }

        @Override
        public Integer get(LocalDate date) {
            return date.getDayOfYear();
        }
    };

private static final DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyyMMdd");
private final List<String> strings;

DateUnitEnum(String... strings) {
    this.strings = Arrays.asList(strings);
}

public static DateUnitEnum get(String unit) {
    for (DateUnitEnum dateUnitEnum : values()) {
        if (dateUnitEnum.strings.contains(unit)) {
            return dateUnitEnum;
        }
    }
    throw new UnsupportedOperationException(String.format("TimeUnitEnum unsupported [%s]", unit));
}

public abstract LocalDate plus(LocalDate date, Integer gap);

public abstract LocalDate minus(LocalDate date, Integer gap);

public abstract Integer get(LocalDate date);

}
