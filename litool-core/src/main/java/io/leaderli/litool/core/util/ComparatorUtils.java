package io.leaderli.litool.core.util;

import java.util.Comparator;

public class ComparatorUtils {


    public static final Comparator<Number> COMPARE_NUMBER = ComparatorUtils::compareNum;

    /**
     * 比较两个数值的大小
     *
     * @param a 第一个数值
     * @param b 第二个数值
     * @return 如果a>b返回1，a=b返回0，a<b返回-1
     */
    public static int compareNum(Number a, Number b) {
        double da = a.doubleValue();
        double db = b.doubleValue();
        return Double.compare(da, db);
    }
}
