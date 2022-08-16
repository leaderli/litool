package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.constant.OperatorEnum;

public class MathCompareInstruct implements Instruct {

    @Override
    public Object apply(Object[] objects) {
        return invoke((OperatorEnum) objects[0], (Number) objects[1], (Number) objects[2]);
    }

    public Boolean invoke(OperatorEnum op, Number left, Number right) {
        return op.apply(left, right);
    }

    public Boolean invoke(OperatorEnum op, Integer left, Integer right) {
        throw new UnsupportedOperationException();
    }


    public Boolean invoke(OperatorEnum op, Double left, Double right) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String name() {
        return "math";
    }
}
