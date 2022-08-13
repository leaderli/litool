package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.xml.router.SequenceElement;

/**
 * @author leaderli
 * @since 2022/8/9 4:49 PM
 */
public interface ElementExecutor<R extends SaxBean & ElementExecutor<R, T>, T extends BaseElementExecutor<R>> {

    default T executor() {
        Class<?> executorClass = ReflectUtil.getGenericInterfacesType(getClass(), ElementExecutor.class, 1).get();
        //noinspection unchecked
        return (T) ReflectUtil.newInstance(executorClass, this).get();

    }
}
