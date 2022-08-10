package io.leaderli.litool.runner.func;

import io.leaderli.litool.core.text.StringUtils;

@Instruct("in")
public class InInstruct {

    public static Boolean invoke(String string, String... searchStrings) {

        return StringUtils.equalsAny(string, searchStrings);
    }

}
