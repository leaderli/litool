package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.runner.constant.OperationAlias;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateCompareInstruct implements Instruct {

    @Override
    public Object apply(Object[] objects) {
        return invoke((String) objects[0], (String) objects[1], (String) objects[2]);
    }

    public Boolean invoke(String op, String left, String right) {
        if (left.trim().length() != 8 || right.trim().length() != 8) {
            throw new IllegalArgumentException(String.format("argument left:[%s] right:[%s] is illegal of Instruct [%s]", left, right, name()));
        }
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate leftDate = LocalDate.parse(left, yyyyMMdd);
        LocalDate rightDate = LocalDate.parse(left, yyyyMMdd);

        op = OperationAlias.getOperation(op);
        switch (op) {
            case ">":
                return leftDate.compareTo(rightDate) > 0;
            case ">=":
                return leftDate.compareTo(rightDate) >= 0;
            case "<":
                return leftDate.compareTo(rightDate) < 0;
            case "<=":
                return leftDate.compareTo(rightDate) <= 0;
            case "=":
                return leftDate.compareTo(rightDate) == 0;
            default:
                throw new UnsupportedOperationException(String.format("operation [%s] is unsupported", op));
        }
    }

    @Override
    public String name() {
        return "date_compare";
    }

}
