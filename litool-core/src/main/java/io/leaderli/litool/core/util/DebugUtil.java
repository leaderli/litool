package io.leaderli.litool.core.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

public class DebugUtil {


    public static boolean isDebugMode() {

        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        List<String> inputArguments = runtimeMXBean.getInputArguments();
        for (String argument : inputArguments) {
            if (argument.contains("jdwp") || argument.contains("debug")) {
                return true;
            }
        }
        return false;
    }
}
