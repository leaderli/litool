
package io.leaderli.litool.core.internal;


import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * UnsafeReflectionAccessor 类是 ReflectionAccessor 接口的一个基础实现，基于 sun.misc.Unsafe 实现。
 * <p>
 * 注意：此实现是为 Java 9 设计的。尽管它应该可以与早期的 Java 版本一起使用，但最好为它们使用 PreJava9ReflectionAccessor。
 */
@SuppressWarnings({"unchecked", "rawtypes"})
final class UnsafeReflectionAccessor extends ReflectionAccessor {

    private static Class unsafeClass;
    private final Object theUnsafe = getUnsafeInstance();
    private final Field overrideField = getOverrideField();

    private static Object getUnsafeInstance() {
        try {
            unsafeClass = Class.forName("sun.misc.Unsafe");
            Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            return unsafeField.get(null);
        } catch (Exception e) {
            return null;
        }
    }

    private static Field getOverrideField() {
        try {
            return AccessibleObject.class.getDeclaredField("override");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 使给定的可访问对象可被访问。
     * <p>
     * 首先使用 sun.misc.Unsafe 实现，如果无法找到则使用基本的 AccessibleObject.setAccessible（boolean）。
     *
     * @param ao 可访问对象
     */
    @Override
    public void makeAccessible(AccessibleObject ao) {
        boolean success = makeAccessibleWithUnsafe(ao);
        if (!success) {
            // unsafe couldn't be found, so try using accessible anyway
            ao.setAccessible(true);
        }
    }

    /**
     * 使用 sun.misc.Unsafe 实现使给定的可访问对象可被访问。
     * <p>
     * 仅用于测试。
     *
     * @param ao 可访问对象
     * @return 是否成功
     */
    boolean makeAccessibleWithUnsafe(AccessibleObject ao) {
        if (theUnsafe != null && overrideField != null) {
            try {
                Method method = unsafeClass.getMethod("objectFieldOffset", Field.class);
                long overrideOffset = (Long) method.invoke(theUnsafe, overrideField);
                Method putBooleanMethod = unsafeClass.getMethod("putBoolean", Object.class, long.class, boolean.class);
                putBooleanMethod.invoke(theUnsafe, ao, overrideOffset, true);
                return true;
            } catch (Exception ignored) { // do nothing
            }
        }
        return false;
    }
}
