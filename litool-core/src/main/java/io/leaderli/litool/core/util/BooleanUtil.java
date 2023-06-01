package io.leaderli.litool.core.util;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.meta.LiValue;

import java.lang.reflect.Array;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/6/16
 */
public class BooleanUtil {


    /**
     * 取反布尔值
     *
     * @param bool 待取反的布尔值
     * @return 取反后的布尔值
     */
    public static boolean negate(boolean bool) {
        return !bool;
    }


    /**
     * 将不同类型的对象转换为布尔值
     * <pl>
     * <li> 如果obj是{@link LiValue}，则返回{@link LiValue#present()}</li>
     * <li> 如果obj是集合，则返回不为空，支持{@link  Iterator}, {@link Iterable},{@link  Map},{@link  Enumeration} </li>
     * <li> 如果obj是数字，则返回不为0 </li>
     * <li> 如果obj是布尔值，则返回obj本身 </li>
     * <li> 如果obj是{@link Array}，则返回{@code arr.length > 0}</li>
     * <li> 其他情况返回{@code obj != null} </li>
     * </ol>
     *
     * @param obj 待转换的对象
     * @return 转换后的布尔值
     */
    public static boolean parse(Object obj) {

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
        if (ArrayUtils.isArray(obj)) {
            return ArrayUtils.getLength(obj) > 0;
        }

        if (obj instanceof Number) {
            return parse((Number) obj);
        }

        return obj != null;
    }

    /**
     * 将{@link LiValue}类型的对象转换为布尔值
     *
     * @param liValue LiValue对象
     * @return {@link LiValue#present()}
     */
    public static boolean parse(LiValue liValue) {
        return liValue != null && liValue.present();
    }

    /**
     * 将{@link Boolean}类型的对象转换为布尔值
     *
     * @param boolValue Boolean对象
     * @return Boolean对象的值
     */
    public static boolean parse(Boolean boolValue) {
        return boolValue != null && boolValue;
    }

    /**
     * 将{@link Iterable}类型的对象转换为布尔值
     *
     * @param iterable Iterable对象
     * @return Iterable对象是否包含元素
     */
    public static boolean parse(Iterable<?> iterable) {
        return iterable != null && iterable.iterator().hasNext();
    }

    /**
     * 将{@link Iterator}类型的对象转换为布尔值
     *
     * @param iterator Iterator对象
     * @return Iterator对象是否包含元素
     */
    public static boolean parse(Iterator<?> iterator) {
        return iterator != null && iterator.hasNext();
    }

    /**
     * 将{@link Enumeration}类型的对象转换为布尔值
     *
     * @param enumeration Enumeration对象
     * @return Enumeration对象是否包含元素
     */
    public static boolean parse(Enumeration<?> enumeration) {
        return enumeration != null && enumeration.hasMoreElements();
    }

    /**
     * 将{@link Map}类型的对象转换为布尔值
     *
     * @param map Map对象
     * @return Map对象是否为空
     */
    public static boolean parse(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    /**
     * 将数字类型的对象转换为布尔值
     *
     * @param number 数字对象
     * @return 数字对象是否不等于0
     */
    private static boolean parse(Number number) {
        return number != null && number.byteValue() != 0;
    }
}
