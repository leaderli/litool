package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.constant.OperatorEnum;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class BetweenTimeInstruct implements Instruct {


    @Override
    public Object apply(Class<?> type, Object[] objects) {
        return invoke((String) objects[0], (String) objects[1], (String) objects[2]);
    }

    public Boolean invoke(String left, String right, String now) {
        DateTimeFormatter HHmm = DateTimeFormatter.ofPattern("HHmm");

        LocalTime leftTime = LocalTime.parse(left, HHmm);
        LocalTime rightTime = LocalTime.parse(right, HHmm);
        LocalTime nowTime = LocalTime.parse(now, HHmm);

        if (leftTime.isBefore(rightTime)) {
            return OperatorEnum.GREATER_THAN.apply(nowTime, leftTime, Comparator.naturalOrder()) && OperatorEnum.LESS_THAN.apply(nowTime,
                    rightTime, Comparator.naturalOrder());
        } else {
            return OperatorEnum.GREATER_THAN.apply(nowTime, leftTime, Comparator.naturalOrder()) || OperatorEnum.LESS_THAN.apply(nowTime,
                    rightTime, Comparator.naturalOrder());
        }

    }

    @Override
    public String name() {
        return "between_time";
    }

    @Override
    public FuncScope getScope() {
        return FuncScope.RUNTIME;
    }
}
