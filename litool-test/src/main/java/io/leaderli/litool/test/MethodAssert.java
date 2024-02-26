package io.leaderli.litool.test;

import java.lang.reflect.Method;

public interface MethodAssert {
    /**
     * @param method  当前方法
     * @param _this   方法调用者，static方法为 null
     * @param args    方法的参数
     * @param _return 放的返回值
     */
    void apply(Method method, Object _this, Object[] args, Object _return);
}
