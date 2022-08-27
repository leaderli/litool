package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;

public class InInstruct implements Instruct {

@Override
public Object apply(Class<?> type, Object[] objects) {
    return invoke((String) objects[0], Lira.of(objects).skip(1).cast(String.class).toArray(String.class));
}

@Override
public String name() {
    return "in";
}

public Boolean invoke(String string, String... searchStrings) {

    return StringUtils.equalsAny(string, searchStrings);
}
}
