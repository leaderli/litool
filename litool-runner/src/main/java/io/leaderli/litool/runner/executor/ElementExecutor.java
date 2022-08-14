package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.dom.sax.SaxBean;

/**
 * @author leaderli
 * @since 2022/8/9 4:49 PM
 */
public interface ElementExecutor<S extends SaxBean & ElementExecutor<S, E>, E extends BaseElementExecutor<S>> {

    default E executor() {
        Class<?> executorClass = ReflectUtil.getGenericInterfacesType(getClass(), ElementExecutor.class, 1).get();
        //noinspection unchecked
        return (E) ReflectUtil.newInstance(executorClass, this).get();

    }
}
