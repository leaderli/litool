package io.leaderli.litool.runner.func;

import io.leaderli.litool.core.text.StringUtils;

@InnerFunc("in")
public class InFunc {

    public static Boolean invoke(String string, String... searchStrings) {

        return StringUtils.equalsAny(string, searchStrings);
    }

}
