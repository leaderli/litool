package io.leaderli.litool.runner.instruct;

/**
 * @author leaderli
 * @since 2022/8/16 3:17 PM
 */
public class AddInstruct implements Instruct {
    @Override
    public Object apply(Class<?> type, Object[] objects) {
        Object left = objects[0];
        Object right = objects[1];
        if (type == Integer.class) {
            return invoke((Integer) left, (Integer) right);
        }
        if (type == Double.class) {

            return invoke((Double) left, (Double) right);
        }

        throw new UnsupportedOperationException();
    }


    public Integer invoke(Integer left, Integer right) {
        return left - right;
    }


    public Double invoke(Double left, Double right) {
        return left - right;
    }

    @Override
    public String name() {
        return "add";
    }
}
