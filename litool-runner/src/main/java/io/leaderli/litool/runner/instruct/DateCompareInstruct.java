package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.constant.OperatorEnum;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateCompareInstruct implements Instruct {

    @Override
    public Object apply(Object[] objects) {
        return invoke((OperatorEnum) objects[0], (String) objects[1], (String) objects[2]);
    }

    public Boolean invoke(OperatorEnum op, String left, String right) {
        if (left.trim().length() != 8 || right.trim().length() != 8) {
            throw new IllegalArgumentException(String.format("argument left:[%s] right:[%s] is illegal of Instruct [%s]", left, right, name()));
        }
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate leftDate = LocalDate.parse(left, yyyyMMdd);
        LocalDate rightDate = LocalDate.parse(right, yyyyMMdd);

        return op.apply(leftDate, rightDate);
    }

    @Override
    public String name() {
        return "date_compare";
    }

}
