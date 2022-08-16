package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.core.text.StringUtils;

public class NotEmptyInstruct implements Instruct {

    @Override
    public Object apply(Class<?> type, Object[] objects) {
        return invoke((String) objects[0]);
    }

    public Boolean invoke(String str) {
        return StringUtils.isNotBlank(str);
    }

    @Override
    public String name() {
        return "not_empty";
    }

}
