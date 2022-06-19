package io.leaderli.litool.core.type;

/**
 * 显式的实现该接口，用于表明你将要用到的泛型
 *
 * @param <T> 泛型
 */
public interface ComponentType<T> {

    /**
     * @return 返回你将要使用的泛型
     */
    Class<T> componentType();

}
