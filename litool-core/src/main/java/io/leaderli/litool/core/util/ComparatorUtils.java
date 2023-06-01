package io.leaderli.litool.core.util;

import java.util.Comparator;

public class ComparatorUtils {


    public static final Comparator<Number> COMPARE_NUMBER = ComparatorUtils::compareNum;

    public static int compareNum(Number a, Number b) {
        double da = a.doubleValue();
        double db = b.doubleValue();
        return Double.compare(da, db);
    }
}
