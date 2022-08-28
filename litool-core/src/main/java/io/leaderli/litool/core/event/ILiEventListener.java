package io.leaderli.litool.core.event;


import io.leaderli.litool.core.type.ComponentType;
import io.leaderli.litool.core.type.ReflectUtil;

/**
 * Listener to listen the specific event, When {@link  LiEventBus#push(Object)} pushed event,
 * the listener which can listen this event
 * <ul>
 *    <li>1. invoke {@link  #before(Object)} check whether {@link  #listen(Object)} should be called</li>
 *    <li>2. call {@link  #listen(Object)}</li>
 *    <li>3. call {@link  #after(LiEventBusBehavior)}, which can
 *    unregister self by {@link LiEventBus#unRegisterListener(ILiEventListener)}</li>
 *
 * </ul>
 *
 * @param <T> the type of  listened event
 * @see LiEventBus
 */
public interface ILiEventListener<T> extends ComponentType<T> {

/**
 * When {@link  LiEventBus#push(Object)} pushed event, the listener which can
 * listen this event will invoke {@link  #before(Object)} check whether this method should be called
 *
 * @param event the listened event
 * @see LiEventBus#push(Object)
 */
void listen(T event);

/**
 * Return whether  the {@link  #listen(Object)} method should be called
 *
 * @param event the listened event
 * @return whether  the {@link  #listen(Object)} method should be called
 */
default boolean before(T event) {
    return true;
}

/**
 * only invoked after {@link  #listen(Object)},
 *
 * @param eventBusBehavior Provide some limited  method for listeners to use
 * @see #before(Object)
 * @see #listen(Object)
 */
@SuppressWarnings("java:S1874")
default void after(LiEventBusBehavior<T> eventBusBehavior) {

    if (removeIf()) {
        eventBusBehavior.unRegisterListener(this);
    }
}

/**
 * Whether to uninstall the listener after listening
 *
 * @return Whether to uninstall the listener after listening
 * @see LiEventBus#unRegisterListener(ILiEventListener)
 * @see LiEventBus#push(Object)
 * @deprecated
 */
@Deprecated
default boolean removeIf() {
    return false;
}

@SuppressWarnings("unchecked")
@Override
default Class<T> componentType() {
    return (Class<T>) ReflectUtil.getGenericInterfacesType(getClass(), ILiEventListener.class).get();
}


}
