package io.leaderli.litool.test;

import java.lang.reflect.Method;

public interface MethodAssert {
    /**
     * @param method       当前方法
     * @param args         方法的参数
     * @param originReturn 放的返回值
     */
    void apply(Method method, Object[] args, Object originReturn);

    default void apply(Method method, Object bean, Object[] args, Object originReturn) {
        apply(method, args, originReturn);
    }
}
