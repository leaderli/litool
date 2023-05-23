package io.leaderli.litool.core.util;

import java.text.DecimalFormat;

/**
 * @author leaderli
 * @since 2022/9/15 8:40 AM
 */
public class HumanSizeUtil {
    private static final DecimalFormat DEC_FORMAT = new DecimalFormat("#.##");

    /**
     * 将内存或者存储容量转换为最适合阅读的大小，以KB、MB、GB、TB、PB、EB的形式显示，根据实际容量大小进行转换
     *
     * @param capacity 内存或者存储容量
     * @return 以KB、MB、GB、TB、PB、EB的形式显示的最适合阅读的大小转换结果
     */
    public static String convertToHumanReadableSize(long capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Invalid file capacity: " + capacity);
        }
        if (capacity < 1024) {
            return capacity + " Bytes";
        }
        int unitIdx = (63 - Long.numberOfLeadingZeros(capacity)) / 10;
        return formatSize(capacity, 1L << (unitIdx * 10), " KMGTPE".charAt(unitIdx) + "B");
    }

    private static String formatSize(long size, long divider, String unitName) {
        return DEC_FORMAT.format((double) size / divider) + " " + unitName;
    }
}
