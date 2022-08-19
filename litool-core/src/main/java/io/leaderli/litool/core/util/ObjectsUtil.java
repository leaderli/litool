package io.leaderli.litool.core.util;

import io.leaderli.litool.core.exception.UnsupportedTypeException;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/6/16 1:02 PM
 */
public class ObjectsUtil {
    public static void requireNotNull(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                throw new RuntimeException();
            }
        }
    }

    public static void requireNotNull(String msg, Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                throw new RuntimeException(msg);
            }
        }
    }

    public static boolean sameAny(Object origin, Object... compares) {


        for (Object compare : compares) {
            if (compare == origin) {
                return true;
            }
        }
        return false;

    }

    private static int compareNumber(Number a, Number b) {

        Double aa = a.doubleValue();
        Double bb = b.doubleValue();
        return aa.compareTo(bb);
    }

    public static <T> int compare(T left, T right) {

        if (left == null && right == null) {

            return 0;
        }
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);

        if (left instanceof Number) {
            return compareNumber((Number) left, (Number) right);
        }

        if (left instanceof String) {
            return ((String) left).compareTo((String) right);
        }

        if (left instanceof Boolean) {
            return ((Boolean) left).compareTo((Boolean) right);
        }
        if (left.getClass().isArray()) {
            return compareNumber(Array.getLength(left), Array.getLength(right));
        }
        if (left instanceof ChronoLocalDate) {
            return ((ChronoLocalDate) left).compareTo((ChronoLocalDate) right);
        }

        if (left instanceof LocalTime) {
            return ((LocalTime) left).compareTo((LocalTime) right);
        }


        throw new UnsupportedTypeException(left.getClass());

    }




}
