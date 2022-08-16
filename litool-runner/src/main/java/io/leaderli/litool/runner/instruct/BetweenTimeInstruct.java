package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.constant.OperatorEnum;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

        if (leftTime.compareTo(rightTime) < 0) {
            return OperatorEnum.GREATER_THAN.apply(nowTime, leftTime) && OperatorEnum.LESS_THAN.apply(nowTime, rightTime);
        } else {
            return OperatorEnum.GREATER_THAN.apply(nowTime, leftTime) || OperatorEnum.LESS_THAN.apply(nowTime, rightTime);
        }

    }

    public static void main(String[] args) {
        BetweenTimeInstruct betweenTimeInstruct = new BetweenTimeInstruct();
        Boolean invoke = betweenTimeInstruct.invoke("0700", "1900", "1200");
        System.out.println(invoke);
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
