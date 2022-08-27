package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.meta.Lino;

/**
 * @author leaderli
 * @since 2022/8/27
 */
public interface BeanFunction<T, R> {

Lino<R> apply(Lino<T> lino);

}
