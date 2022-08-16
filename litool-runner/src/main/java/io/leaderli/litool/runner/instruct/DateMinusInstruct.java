package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.constant.DateUnitEnum;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateMinusInstruct implements Instruct {

    @Override
    public Object apply(Object[] objects) {
        return invoke((String) objects[0], (DateUnitEnum) objects[1], (Integer) objects[2]);
    }

    public String invoke(String dateStr, DateUnitEnum unit, Integer gap) {
        if (dateStr.trim().length() != 8) {
            throw new IllegalArgumentException(String.format("argument dateStr:[%s] is illegal of Instruct [%s]", dateStr, name()));
        }
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(dateStr, yyyyMMdd);

        LocalDate plusResult = unit.minus(date, gap);

        return plusResult.format(yyyyMMdd);
    }

    @Override
    public String name() {
        return "date_minus";
    }
}
