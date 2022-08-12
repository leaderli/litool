package io.leaderli.litool.runner.instruct;

import com.sun.org.apache.xpath.internal.functions.FuncBoolean;
import io.leaderli.litool.runner.constant.OperationAlias;

import java.util.HashMap;
import java.util.Map;

public class MathInstruct implements Instruct {

    @Override
    public Object apply(Object[] objects) {
        return invoke((String) objects[0], (Integer) objects[1], (Integer) objects[2]);
    }

    public Boolean invoke(String op, Integer left, Integer right) {
        op = OperationAlias.getOperation(op);
        switch (op) {
            case ">":
                return left.compareTo(right) > 0;
            case ">=":
                return left.compareTo(right) >= 0;
            case "<":
                return left.compareTo(right) < 0;
            case "<=":
                return left.compareTo(right) <= 0;
            case "=":
                return left.compareTo(right) == 0;
            default:
                throw new UnsupportedOperationException(String.format("operation [%s] is unsupported", op));
        }
    }

    @Override
    public String name() {
        return "math";
    }
}
