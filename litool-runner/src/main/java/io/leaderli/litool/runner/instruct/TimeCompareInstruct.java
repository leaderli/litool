package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.constant.OperatorEnum;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class TimeCompareInstruct implements Instruct {

    @Override
    public Object apply(Class<?> type, Object[] objects) {
        return invoke((OperatorEnum) objects[0], (String) objects[1], (String) objects[2]);
    }

    public Boolean invoke(OperatorEnum op, String left, String right) {
        if (left.trim().length() != 4 || right.trim().length() != 4) {
            throw new IllegalArgumentException(String.format("argument left:[%s] right:[%s] is illegal of Instruct " +
                            "[%s]",
                    left, right, name()));
        }
        DateTimeFormatter HHmm = DateTimeFormatter.ofPattern("HHmm");
        LocalTime leftTime = LocalTime.parse(left, HHmm);
        LocalTime rightTime = LocalTime.parse(right, HHmm);

        return op.apply(leftTime, rightTime, Comparator.naturalOrder());
    }

    @Override
    public String name() {
        return "time_compare";
    }

}
