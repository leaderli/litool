package io.leaderli.litool.core.type;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.meta.Lira;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/26 8:41 AM
 */
public class MethodScanner {


    /**
     * 查找类
     */
    private final Class<?> cls;
    /**
     * 是否查找私有方法
     *
     * @see Class#getDeclaredMethods()
     */
    private final boolean scan_private;

    /**
     * 方法过滤器
     *
     * @see io.leaderli.litool.core.util.BooleanUtil#parse(Object)
     */
    private final Function<Method, ?> filter;

    public MethodScanner(Class<?> cls, boolean scan_private, Function<Method, ?> filter) {
        this.cls = cls;
        this.scan_private = scan_private;
        this.filter = filter;
    }

    public static MethodScanner of(Class<?> cls, boolean scan_private, Function<Method, ?> filter) {
        return new MethodScanner(cls, scan_private, filter);
    }

    /**
     * 移除所有 object 的方法
     *
     * @return 扫描方法
     * @see #filter
     * @see #scan_private
     */
    public Lira<Method> scan() {
        if (cls != null) {
            Lira<Method> methods = Lira.of(cls.getMethods());
            if (scan_private) {
                methods = CollectionUtils.union(methods, Lira.of(cls.getDeclaredMethods()));
            }
            return methods
                    .filter(MethodUtil::notObjectMethod)
                    .filter(filter);


        }
        return Lira.none();
    }


}
