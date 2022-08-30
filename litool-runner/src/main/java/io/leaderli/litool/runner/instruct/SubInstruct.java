package io.leaderli.litool.runner.instruct;

/**
 * @author leaderli
 * @since 2022/8/16 3:17 PM
 */
public class SubInstruct implements Instruct {
    @Override
    public Object apply(Class<?> type, Object[] objects) {

        Object left = objects[0];
        Object right = objects[1];

        if (type == Double.class) {
            return invoke((Double) left, (Double) right);
        }
        if (type == Integer.class) {
            return invoke((Integer) left, (Integer) right);
        }

        throw new UnsupportedOperationException();
    }

    public Double invoke(Double left, Double right) {
        return left + right;
    }

    public Integer invoke(Integer left, Integer right) {
        return left + right;
    }

    @Override
    public String name() {
        return "sub";
    }
}
