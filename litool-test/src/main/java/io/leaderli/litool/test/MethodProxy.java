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

    /**
     * 增加多线程支持，避免相互影响
     */
    static MethodProxy<?> threadLocal(MethodProxy<?> methodProxy) {

        Thread currentThread = Thread.currentThread();
        return (m, args) -> {
            if (Thread.currentThread() == currentThread) {
                return methodProxy.apply(m, args);
            }
            return LiMock.SKIP_MARK;
        };
    }

    T apply(Method method, Object[] args) throws Throwable;

}
