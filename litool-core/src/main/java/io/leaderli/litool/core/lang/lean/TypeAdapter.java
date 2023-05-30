package io.leaderli.litool.core.lang.lean;

import java.lang.reflect.Type;

/**
 * 类型适配器接口，用于将源对象转换为目标对象
 *
 * @param <T> 目标对象类型参数
 */
@FunctionalInterface
public interface TypeAdapter<T> {

    /**
     * 将源对象转换为目标对象
     *
     * @param source 源对象
     * @param lean   提供适配器的Lean对象，可以通过{@link Lean#getTypeAdapter(Type)}获取自定义适配器
     *               以执行一些特殊操作
     * @return 转换后的目标对象
     */
    T read(Object source, Lean lean);

    /**
     * 当源对象为null时调用
     *
     * @param lean 提供适配器的Lean对象，可以通过{@link Lean#getTypeAdapter(Type)}获取自定义适配器
     *             以执行一些特殊操作
     * @return 转换后的目标对象
     * @see #read(Object, Lean)
     */
    default T read(Lean lean) {
        return null;
    }
}
