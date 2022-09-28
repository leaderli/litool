package io.leaderli.litool.core.type;

/**
 * Explicitly implement this interface indicate the generics you will use
 *
 * @param <T> the type of indicate
 */
public interface ComponentType<T> {

    /**
     * Provide a avoid null pointer method to get indicate type, it will return null
     * if component is null
     *
     * @param component the componentType implement instance
     * @param <T>       the class of componentType
     * @param <R>       the generic type of the componentType
     * @return the real type of componentType indicate type
     */
    static <T extends ComponentType<R>, R> Class<R> componentType(T component) {

        if (component != null) {
            return component.componentType();
        }
        return null;
    }

    /**
     * @return the class of you indicate oine
     */
    @SuppressWarnings("unchecked")
    default Class<T> componentType() {
        return (Class<T>) TypeUtil.resolve2Parameterized(getClass(), ComponentType.class).getActualClassArgument().get();
    }
}
