package io.leaderli.litool.core.internal;

import java.lang.reflect.AccessibleObject;

/**
 * PreJava9ReflectionAccessor 类是 ReflectionAccessor 接口的一个基础实现，在 Java 8 及以下版本中使用。
 * <p>
 * 此实现仅调用 AccessibleObject.setAccessible（true），在 Java 9 之前可以正常工作。
 */
final class PreJava9ReflectionAccessor extends ReflectionAccessor {

    /**
     * 将给定的可访问对象设置为可访问状态。
     *
     * @param accessibleObject 可访问对象
     */
    @Override
    public void makeAccessible(AccessibleObject accessibleObject) {
        accessibleObject.setAccessible(true);
    }
}
