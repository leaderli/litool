package io.leaderli.litool.core.type;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.util.BooleanUtil;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
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
//    /**
//     * 扫描结果集合
//     */
//    private final Set<Method> methods = new HashSet<>();

    public MethodScanner(Class<?> cls, boolean scan_private, Function<Method, ?> filter) {
        this.cls = cls;
        this.scan_private = scan_private;
        this.filter = filter;
    }

    /**
     * @return 不查找 object 的方法
     */
    public Lira<Method> scan() {
        if (cls != null) {
            Method[] methods = cls.getMethods();
            if (scan_private) {
                methods = ArrayUtils.union(methods, cls.getDeclaredMethods());
            }
            return Lira.of(methods)
                    .filter(MethodUtil::notObjectMethod)
                    .filter(filter);

        }
        return Lira.none();
    }


}
