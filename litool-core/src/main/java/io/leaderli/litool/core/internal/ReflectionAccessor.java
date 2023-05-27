package io.leaderli.litool.core.internal;

import java.lang.reflect.AccessibleObject;

/**
 * ReflectionAccessor 类提供了一个替代 AccessibleObject.setAccessible（boolean）的方式，可用于避免在 Java 9 中出现的反射访问问题。
 * <p>
 * 适用于 Java 9 及更早版本。
 */
public abstract class ReflectionAccessor {

    // 单例实例，使用 getInstance() 获取
    @SuppressWarnings("StaticInitializerReferencesSubClass")
    private static final ReflectionAccessor INSTANCE = JavaVersion.getMajorJavaVersion() < 9 ?
            new PreJava9ReflectionAccessor() : new UnsafeReflectionAccessor();

    /**
     * 获取适用于当前 Java 版本的 ReflectionAccessor 实例。
     * <p>
     * 在这种情况下，应该在字段、方法或构造函数上使用 ReflectionAccessor.makeAccessible（AccessibleObject）（而不是基本的 AccessibleObject.setAccessible（boolean））。
     *
     * @return ReflectionAccessor 实例
     */
    public static ReflectionAccessor getInstance() {
        return INSTANCE;
    }

    /**
     * 使给定的可访问对象可被访问。
     *
     * @param ao 可访问对象
     */
    public abstract void makeAccessible(AccessibleObject ao);
}
