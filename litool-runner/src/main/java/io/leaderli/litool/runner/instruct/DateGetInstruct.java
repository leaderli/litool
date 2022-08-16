package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.constant.DateUnitEnum;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateGetInstruct implements Instruct {

    @Override
    public Object apply(Object[] objects) {
        return invoke((String) objects[0], (DateUnitEnum) objects[1]);
    }

    public Integer invoke(String dateStr, DateUnitEnum unit) {
        if (dateStr.trim().length() != 8) {
            throw new IllegalArgumentException(String.format("argument dateStr:[%s] is illegal of Instruct [%s]", dateStr, name()));
        }
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(dateStr, yyyyMMdd);

        return unit.get(date);
    }

    @Override
    public String name() {
        return "date_get";
    }
}
