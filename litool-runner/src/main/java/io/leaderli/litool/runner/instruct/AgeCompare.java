package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.constant.OperationAlias;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AgeCompare implements Instruct {
    @Override
    public Object apply(Object[] objects) {
        return null;
    }

    public Boolean invoke(String op, String birthday, String year) {
        if (birthday.trim().length() != 8) {
            throw new IllegalArgumentException(String.format("argument birthday:[%s] is illegal of Instruct [%s]", birthday, name()));
        }
        int year_int;
        try {
            year_int = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("argument year:[%s] is illegal of Instruct [%s]", year, name()));
        }

        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(birthday, yyyyMMdd);
        LocalDate now = LocalDate.now();

        op = OperationAlias.getOperation(op);
        switch (op) {
            case ">":
                return date.compareTo(now.minusYears(year_int)) < 0;
            case ">=":
                return date.compareTo(now.minusYears(year_int)) <= 0;
            case "<":
                return date.compareTo(now.minusYears(year_int)) > 0;
            case "<=":
                return date.compareTo(now.minusYears(year_int)) >= 0;
            case "=":
                return date.compareTo(now.minusYears(year_int)) == 0;
            default:
                throw new UnsupportedOperationException(String.format("operation [%s] is unsupported", op));
        }
    }

    @Override
    public String name() {
        return null;
    }
}
