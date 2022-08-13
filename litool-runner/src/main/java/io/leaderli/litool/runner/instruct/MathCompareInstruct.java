package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.constant.OperatorEnum;

public class MathCompareInstruct implements Instruct {

    @Override
    public Object apply(Object[] objects) {
        return invoke((OperatorEnum) objects[0], (Integer) objects[1], (Integer) objects[2]);
    }

    public Boolean invoke(OperatorEnum op, Integer left, Integer right) {
        return op.apply(left, right);
    }

    @Override
    public String name() {
        return "math";
    }
}
