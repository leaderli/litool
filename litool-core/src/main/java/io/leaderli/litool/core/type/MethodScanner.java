package io.leaderli.litool.core.type;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.meta.Lira;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * 用于查找类中的方法的工具类
 *
 * @author leaderli
 * @since 2022/7/26 8:41 AM
 */
public class MethodScanner {


    /**
     * 被查找的类
     */
    private final Class<?> targetClass;
    /**
     * 是否查找私有方法
     *
     * @see Class#getDeclaredMethods()
     */
    private final boolean scan_private;

    /**
     * 查找到的方法需要满足的条件
     *
     * @see io.leaderli.litool.core.util.BooleanUtil#parse(Object)
     */
    private final Function<Method, ?> filter;
    /**
     * 是否排除 Object 类的方法
     */
    private boolean excludeObjectMethods = true;

    public MethodScanner(Class<?> targetClass, boolean scan_private, Function<Method, ?> filter) {
        this.targetClass = targetClass;
        this.scan_private = scan_private;
        this.filter = filter;
    }

    /**
     * 创建 MethodScanner 实例
     *
     * @param targetClass 需要查找方法的类
     * @param scanPrivate 是否查找私有方法
     * @param filter      查找到的方法需要满足的条件
     * @return 返回新的 MethodScanner 实例
     */
    public static MethodScanner of(Class<?> targetClass, boolean scanPrivate, Function<Method, ?> filter) {
        return new MethodScanner(targetClass, scanPrivate, filter);
    }

    /**
     * 设置包含 Object 类的方法
     */
    public void includeObjectMethods() {
        this.excludeObjectMethods = false;
    }

    /**
     * 在 targetClass 中查找方法
     *
     * @return 返回一个 Lira 实例，包含所有符合条件的方法
     */
    public Lira<Method> scan() {
        if (targetClass != null) {

            Lira<Method> methods = Lira.of(targetClass.getMethods());

            if (scan_private) {
                methods = CollectionUtils.union(Method.class, methods, Lira.of(targetClass.getDeclaredMethods()));
            }
            if (excludeObjectMethods) {
                methods = methods.filter(MethodUtil::notObjectMethod);
            }


            return methods.filter(m -> !m.isSynthetic()).filter(filter);

        }
        return Lira.none();
    }


}
