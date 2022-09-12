package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.LiValue;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/6/16
 */
public class BooleanUtil {


    /**
     * @param bool boolean value
     * @return !bool
     */
    public static boolean negate(boolean bool) {
        return !bool;
    }

    /**
     * <ul>
     *  <li> if obj is collection, return is not empty, support {@link  Iterator}, {@link Iterable},{@link  Map},
     *  {@link  Enumeration}
     *  </li>
     *  <li> if obj is {@link  Number}, return it's not 0 </li>
     *  <li> if obj is boolean，return it self </li>
     *  <li> if obj is {@link LiValue}, return {@link LiValue#present()}</li>
     *  <li> other  return {@code obj != null} </li>
     * </ul>
     *
     * @param obj the obj
     * @return parse obj to boolean depend on obj class
     */
    public static boolean parse(Object obj) {

        if (obj == null) {
            return false;
        }
        if (obj instanceof LiValue) {
            return parse((LiValue) obj);
        }
        if (obj instanceof Boolean) {
            return parse((Boolean) obj);
        }
        if (obj instanceof Iterable) {
            return parse((Iterable<?>) obj);
        }
        if (obj instanceof Iterator) {
            return parse((Iterator<?>) obj);
        }
        if (obj instanceof Enumeration) {
            return parse((Enumeration<?>) obj);
        }
        if (obj instanceof Map) {
            return parse((Map<?, ?>) obj);
        }

        if (obj instanceof Number) {
            return parse((Number) obj);
        }

        return true;
    }

    /**
     * @param value {@link LiValue}
     * @return {@link LiValue#present()}
     */
    public static boolean parse(LiValue value) {
        return value != null && value.present();
    }

    /**
     * @param value {@link Boolean}
     * @return {@link Boolean#valueOf(String)}
     */
    public static boolean parse(Boolean value) {
        return value != null && value;
    }

    /**
     * @param iterable {@link Iterable#iterator()}
     * @return iterable has element
     */
    public static boolean parse(Iterable<?> iterable) {
        return iterable != null && iterable.iterator().hasNext();
    }

    /**
     * @param iterator {@link Iterator}
     * @return {@link  Iterator#hasNext()}
     */
    public static boolean parse(Iterator<?> iterator) {
        return iterator != null && iterator.hasNext();
    }

    /**
     * @param enumeration {@link Enumeration}
     * @return {@link  Enumeration#hasMoreElements()}
     */
    public static boolean parse(Enumeration<?> enumeration) {
        return enumeration != null && enumeration.hasMoreElements();
    }

    /**
     * @param map {@link Map}
     * @return {@code !Map.isEmpty()}
     */
    public static boolean parse(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    /**
     * @param value number
     * @return {@code  number!=0}
     */
    private static boolean parse(Number value) {
        return value != null && value.byteValue() != 0;
    }
}
