package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.core.text.StringUtils;
import org.joda.time.DateTime;

import java.lang.reflect.Method;

public class NowInstruct implements Instruct {

    @Override
    public Object apply(Object[] objects) {
        return invoke((String)objects[0], (String)objects[1], (String) objects[2]);
    }

    public static String invoke(String formatStr, String currentDate, String test) {

        if (StringUtils.isBlank(formatStr)) {
            formatStr = "yyyyMMdd";
        }
        if (StringUtils.equals(test, "local_test")) {
            return currentDate;
        }
        return DateTime.now().toString(formatStr);
    }

    @Override
    public String name() {
        return "now";
    }

    @Override
    public FuncScope getScope() {
        return FuncScope.RUNTIME;
    }
}
