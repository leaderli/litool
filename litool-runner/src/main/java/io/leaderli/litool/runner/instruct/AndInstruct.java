package io.leaderli.litool.runner.instruct;

public class AndInstruct implements Instruct {

    @Override
    public Object apply(Object[] objects) {
        return invoke((Boolean[]) objects);
    }

    @Override
    public String name() {
        return "and";
    }

    public static Boolean invoke(Boolean... ands) {

        for (Boolean and : ands) {
            if (!and) {
                return false;
            }
        }
        return true;
    }
}
