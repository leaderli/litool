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
                throw new NullPointerException();
            }
        }
    }

    public static void requireNotNull(String msg, Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                throw new NullPointerException(msg);
            }
        }
    }

    /**
     * @param comparing the comparing obj
     * @param compares  the compared array
     * @return compares elements have any element same as comparing
     */
    public static boolean sameAny(Object comparing, Object... compares) {


        for (Object compare : compares) {
            if (compare == comparing) {
                return true;
            }
        }
        return false;

    }

    /**
     * <ul>
     * <li>{@link  Number}, return {@code  a - b }</li>
     * <li>Array, return {@code  a.length - b.length }</li>
     * <li>{@link  String#compareTo(String)} </li>
     * <li>{@link  Boolean#compareTo(Boolean)} </li>
     * <li>{@link  ChronoLocalDate#compareTo(ChronoLocalDate)} </li>
     * <li>{@link  LocalTime#compareTo(LocalTime)} </li>
     * </ul>
     *
     * @param left  left object
     * @param right right object
     * @param <T>   the type of two parameter
     * @return the union compare  of several class
     */
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

    private static int compareNumber(Number a, Number b) {

        Double aa = a.doubleValue();
        Double bb = b.doubleValue();

        return aa.compareTo(bb);
    }


    public static int hashCodeOrZero(Object o) {
        return o != null ? o.hashCode() : 0;
    }

    /**
     * @param a an object
     * @param b an object to be compared with {@code a} for equality
     * @return {@code true} if the arguments are not equal to each other
     * and {@code false} otherwise
     */
    @SuppressWarnings("all")
    public static boolean notEquals(Object a, Object b) {
        return a != b && (a == null || !a.equals(b));
    }
}
