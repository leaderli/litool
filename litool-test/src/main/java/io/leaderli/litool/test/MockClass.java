package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ModifierUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MockClass {
    public final Class<?> clazz;
    /**
     * 表示类是否已经修改过
     */
    public boolean redefine;

    /**
     * 方法签名和方法的映射
     */
    private final Map<String, Method> methodSignature;
    /**
     * 表示类所有方法的挡板
     */
    private final Map<String, List<MethodProxy<?>>> methodProxyList;

    public MockClass(Class<?> clazz) {
        this.clazz = clazz;
        Lira<Method> methods = Lira.of(clazz.getDeclaredMethods()).filter(method -> !(method.isSynthetic() || ModifierUtil.isAbstract(method)));
        methodSignature = Collections.unmodifiableMap(methods.toMap(m -> m + "", m -> m));
        methodProxyList = Collections.unmodifiableMap(methods.toMap(m -> m + "", m -> new ArrayList<>()));
    }
}
