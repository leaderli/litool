package io.leaderli.litool.test;

import io.leaderli.litool.core.type.ReflectUtil;

import java.lang.reflect.Method;

public interface MethodProxy<T> {

    MethodProxy<?> NONE = of(null);

    static <T> MethodProxy<T> of(T value) {
        return (m, args) -> value;
    }

    static <T> MethodProxy<T> error(Throwable throwable) {
        return (m, args) -> {
            throw throwable;
        };
    }

    /**
     * @return 根据返回类型生成一个默认值
     */
    static MethodProxy<?> of() {
        return (m, args) -> ReflectUtil.newInstance(m.getReturnType()).assertNotNone().get();
    }

    T apply(Method method, Object[] args) throws Throwable;

}
