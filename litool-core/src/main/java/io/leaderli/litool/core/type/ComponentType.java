package io.leaderli.litool.core.type;

/**
 * 用于明确指定使用的泛型类型
 *
 * @param <T> 泛型
 */
public interface ComponentType<T> {


    /**
     * 从组件获取指定类型，若组件为 null 则返回 null
     *
     * @param component 组件类型实例
     * @param <T>       组件类型
     * @param <R>       组件泛型类型
     * @return 组件指定类型的实际类型
     */
    static <T extends ComponentType<R>, R> Class<R> componentType(T component) {

        if (component != null) {
            return component.componentType();
        }
        return null;
    }

    /**
     * 获取组件指定类型的实际类型，一般情况下接口需要显式的重写它
     *
     * @return 指定类型的实际类型
     */
    @SuppressWarnings("unchecked")
    default Class<T> componentType() {
        return (Class<T>) TypeUtil.resolve2Parameterized(getClass(), ComponentType.class).getActualClassArgument().get();
    }
}
