package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.dom.sax.SaxBean;

/**
 * @author leaderli
 * @since 2022/8/9 4:49 PM
 */
public interface ElementExecutor<S extends SaxBean & ElementExecutor<S, E>, E extends BaseElementExecutor<S>> {

    @SuppressWarnings({"unchecked", "java:S1452"})
    default BaseElementExecutor<?> executor() {
        // 获取继承类指明的第二个泛型类
        Class<?> executorClass = ReflectUtil.getDeclareClassAt(getClass(), ElementExecutor.class, 1).get();
        return (E) ReflectUtil.newInstance(executorClass, this).get();

    }
}
