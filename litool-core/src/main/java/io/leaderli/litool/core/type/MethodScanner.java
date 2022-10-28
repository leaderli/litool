package io.leaderli.litool.core.type;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.meta.Lira;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * a tool to help find method of class
 *
 * @author leaderli
 * @since 2022/7/26 8:41 AM
 */
public class MethodScanner {


    /**
     * the class being look up
     */
    private final Class<?> cls;
    /**
     * whether find private method
     *
     * @see Class#getDeclaredMethods()
     */
    private final boolean scan_private;

    /**
     * the filter of found method
     *
     * @see io.leaderli.litool.core.util.BooleanUtil#parse(Object)
     */
    private final Function<Method, ?> filter;
    /**
     * exclude object method
     */
    private boolean not_scan_object = true;

    public MethodScanner(Class<?> cls, boolean scan_private, Function<Method, ?> filter) {
        this.cls = cls;
        this.scan_private = scan_private;
        this.filter = filter;
    }

    public static MethodScanner of(Class<?> cls, boolean scan_private, Function<Method, ?> filter) {
        return new MethodScanner(cls, scan_private, filter);
    }

    public void set_scan_object() {
        this.not_scan_object = false;
    }

    /**
     * the lira of found method, the scope of the lookup method according to {@link  #scan_private}
     * , {@link  #not_scan_object} and {@link  #filter}
     *
     * @return the found methods
     * @see #filter
     * @see #scan_private
     */
    public Lira<Method> scan() {
        if (cls != null) {

            Lira<Method> methods = Lira.of(cls.getMethods());

            if (scan_private) {
                methods = CollectionUtils.union(methods, Lira.of(cls.getDeclaredMethods()));
            }
            if (not_scan_object) {
                methods = methods.filter(MethodUtil::notObjectMethod);
            }


            return methods.filter(m -> !m.isSynthetic()).filter(filter);

        }
        return Lira.none();
    }


}
