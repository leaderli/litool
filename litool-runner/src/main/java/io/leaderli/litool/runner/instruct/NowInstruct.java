package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.core.text.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class NowInstruct implements Instruct {

    @Override
    public Object apply(Class<?> type, Object[] objects) {
        return invoke((String) objects[0], (String) objects[1], (String) objects[2]);
    }

    public String invoke(String formatStr, String currentDate, String test) {

        if (StringUtils.isBlank(formatStr)) {
            formatStr = "yyyyMMdd";
        }
        if (StringUtils.equals(test, "local_test")) {
            return currentDate;
        }
        return LocalDate.now().format(DateTimeFormatter.ofPattern(formatStr));
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
