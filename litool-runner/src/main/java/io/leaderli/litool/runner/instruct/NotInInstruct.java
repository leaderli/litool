package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;

public class NotInInstruct implements Instruct {
    @Override
    public Object apply(Object[] objects) {
        return invoke((String) objects[0], Lira.of(objects).skip(1).cast(String.class).toArray(String.class));
    }

    public Boolean invoke(String str, String... searchStrings) {

        return StringUtils.equalsAny(str, searchStrings);
    }

    @Override
    public String name() {
        return "notin";
    }

}
