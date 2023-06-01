package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.constant.OperatorEnum;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class AgeCompareInstruct implements Instruct {
    @Override
    public Object apply(Class<?> type, Object[] objects) {
        return invoke((OperatorEnum) objects[0], (String) objects[1], (String) objects[2]);
    }

    public Boolean invoke(OperatorEnum op, String birthday, String year) {
        if (birthday.trim().length() != 8) {
            throw new IllegalArgumentException(String.format("argument birthday:[%s] is illegal of Instruct [%s]",
                    birthday, name()));
        }
        int year_int;
        try {
            year_int = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("argument year:[%s] is illegal of Instruct [%s]", year,
                    name()));
        }

        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(birthday, yyyyMMdd);
        LocalDate now = LocalDate.now();

        return !op.apply(date, now.minusYears(year_int), Comparator.naturalOrder());
    }

    @Override
    public String name() {
        return "age_compare";
    }
}
