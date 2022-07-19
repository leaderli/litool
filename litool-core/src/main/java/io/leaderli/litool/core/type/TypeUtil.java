package io.leaderli.litool.core.type;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public class TypeUtil {

    /**
     * 是否未知类型<br>
     * type为null或者{@link TypeVariable} 都视为未知类型
     *
     * @param type Type类型
     * @return 是否未知类型
     */
    public static boolean isUnknown(Type type) {
        return null == type || type instanceof TypeVariable;
    }
}
