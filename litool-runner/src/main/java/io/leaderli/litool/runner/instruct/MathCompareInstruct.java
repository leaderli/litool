package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.constant.OperatorEnum;

public class MathCompareInstruct implements Instruct {

@Override
public Object apply(Class<?> type, Object[] objects) {
    return invokeNumber((OperatorEnum) objects[0], (Number) objects[1], (Number) objects[2]);
}

public Boolean invokeNumber(OperatorEnum op, Number left, Number right) {
    return op.apply(left, right);
}

@Override
public String name() {
    return "math";
}

public Boolean invoke(OperatorEnum op, Integer left, Integer right) {
    throw new UnsupportedOperationException();
}

public Boolean invoke(OperatorEnum op, Double left, Double right) {
    throw new UnsupportedOperationException();
}
}
