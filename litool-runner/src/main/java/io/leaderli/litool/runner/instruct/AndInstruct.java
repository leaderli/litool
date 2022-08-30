package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.core.meta.Lira;

public class AndInstruct implements Instruct {

    @Override
    public Object apply(Class<?> type, Object[] objects) {
        return invoke(Lira.of(objects).cast(Boolean.class).toArray(Boolean.class));
    }

    @Override
    public String name() {
        return "and";
    }

    public Boolean invoke(Boolean... ands) {

        for (boolean and : ands) {
            if (!and) {
                return false;
            }
        }
        return true;
    }

}
