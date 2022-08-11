package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.core.meta.Lira;

public class AndInstruct implements Instruct {

    @Override
    public Object apply(Object[] objects) {
        return invoke(Lira.of(objects).cast(Boolean.class).toArray(Boolean.class));
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
