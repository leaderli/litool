package io.leaderli.litool.core.event;


import io.leaderli.litool.core.function.OnError;
import io.leaderli.litool.core.type.ComponentType;
import io.leaderli.litool.core.type.ReflectUtil;

/**
 * Listener to listen the specific event, When {@link  LiEventBus#push(Object)} pushed event,
 * the listener which can listen this event
 * <ul>
 *    <li>1. invoke {@link  #before(Object)} check whether {@link  #listen(Object)} should be called</li>
 *    <li>2. call {@link  #listen(Object)}</li>
 *    <li>3. call {@link  #onError(Throwable)} when {@link  #listen(Object)} throw a Throwable </li>
 *    <li>4. call {@link  #after(LiEventBusBehavior)} after {@link  #listen(Object)} has success executed.
 *    you can unregister self by {@link LiEventBus#unRegisterListener(ILiEventListener)}</li>
 *
 * </ul>
 *
 * @param <T> the type of  listened event
 * @see LiEventBus
 */
@FunctionalInterface
public interface ILiEventListener<T> extends ComponentType<T>, OnError {

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
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    default boolean removeIf() {
        return false;
    }

    /**
     * perform action on error occur at  {@link #listen(Object)}
     *
     * @param throwable the error thrown by {@link  #listen(Object)}
     * @see LiEventBus#push(Object)
     */

    @Override
    default void onError(Throwable throwable) {

        throwable.printStackTrace();
    }

    @SuppressWarnings("unchecked")
    @Override
    default Class<T> componentType() {
        return (Class<T>) ReflectUtil.getDeclareTypeHead(getClass(), ILiEventListener.class).get();
    }


}
