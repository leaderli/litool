package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.text.StringUtils;

public class InInstruct implements Instruct {

    @Override
    public Object apply(Object[] objects) {
        return invoke((String) objects[0], (String[]) ArrayUtils.sub(objects, 1, 0));
    }

    @Override
    public String name() {
        return "in";
    }

    public static Boolean invoke(String string, String... searchStrings) {

        return StringUtils.equalsAny(string, searchStrings);
    }
}
