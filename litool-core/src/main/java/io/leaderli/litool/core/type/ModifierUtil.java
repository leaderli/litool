package io.leaderli.litool.core.type;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author leaderli
 * @since 2022/8/9
 */
public class ModifierUtil {

    public static boolean isPublic(Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    public static boolean isStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }
}
