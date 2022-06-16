package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.LiValue;

import java.util.Iterator;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/6/16
 */
public class LiBoolUtil {

    /**
     * @param obj 实例
     * @return 根据 obj 的 类型来转换 obj 的 布尔类型
     *     <ul>
     *
     * <li>当 {@code obj == null} 返回 false</li>
     * <li> 当 obj 为集合类时，返回其是否包含元素</li>
     * <li>当 obj 为 boolean 时，返回 obj 即可</li>
     * <li>当 为 {@link LiValue} 时 返回 {@link LiValue#isPresent()}</li>
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

        return true;
    }


    private static boolean parse(LiValue value) {
        return value.isPresent();
    }

    private static boolean parse(Boolean value) {
        return value;
    }


    private static boolean parse(Iterable<?> iterable) {
        return iterable.iterator().hasNext();
    }


    private static boolean parse(Iterator<?> iterator) {
        return iterator.hasNext();
    }


    private static boolean parse(Map<?, ?> map) {
        return !map.isEmpty();
    }
}
