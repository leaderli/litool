package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.core.text.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeInstruct implements Instruct {

@Override
public Object apply(Class<?> type, Object[] objects) {
    return invoke((String) objects[0], (String) objects[1], (String) objects[2]);
}

public String invoke(String formatStr, String currentDate, String test) {

    if (StringUtils.isBlank(formatStr)) {
        formatStr = "HHmm";
    }
    if (StringUtils.equals(test, "local_test")) {
        return currentDate;
    }
    return LocalTime.now().format(DateTimeFormatter.ofPattern(formatStr));
}


@Override
public String name() {
    return "time";
}

@Override
public FuncScope getScope() {
    return FuncScope.RUNTIME;
}
}
