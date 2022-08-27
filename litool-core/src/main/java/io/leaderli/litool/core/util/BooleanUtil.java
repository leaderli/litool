package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.LiValue;

import java.util.Iterator;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/6/16
 */
public class BooleanUtil {

/**
 * @param obj 实例
 * @return 根据 obj 的 类型来转换 obj 的 布尔类型
 *     <ul>
 *
 * <li>当 {@code obj == null} 返回 false</li>
 * <li> 当 obj 为集合类时，返回其是否包含元素</li>
 * <li> 当 obj 为数值时，返回其是否不为0</li>
 * <li>当 obj 为 boolean 时，返回 obj 即可</li>
 * <li>当 为 {@link LiValue} 时 返回 {@link LiValue#present()}</li>
 * <li>其他情况返回 true</li>
 *     </ul>
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
 * @return 迭代器中包含元素
 */
public static boolean parse(Iterable<?> iterable) {
    return iterable != null && iterable.iterator().hasNext();
}

/**
 * @param iterator {@link Iterator}
 * @return 迭代器中包含元素
 */
public static boolean parse(Iterator<?> iterator) {
    return iterator != null && iterator.hasNext();
}

/**
 * @param map {@link Map}
 * @return map 的 集合不为空
 */
public static boolean parse(Map<?, ?> map) {
    return map != null && !map.isEmpty();
}

/**
 * @param value 数值
 * @return 返回数值不为0
 */
public static boolean parse(Number value) {
    return value != null && value.byteValue() != 0;
}
}
