package io.leaderli.litool.core.type;

/**
 * Explicitly implement this interface indicate the generics you will use
 *
 * @param <T> the type of indicate
 */
public interface ComponentType<T> {

/**
 * @return the class of you indicate oine
 */
@SuppressWarnings("unchecked")
default Class<T> componentType() {
    return (Class<T>) ReflectUtil.getGenericInterfacesType(getClass(), ComponentType.class).get();
}


}
