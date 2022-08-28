package io.leaderli.litool.core.event;

/**
 * Provide some limited  method for listeners to use
 *
 * @param <T> the type provided by {@link  ILiEventListener#componentType()}
 * @author leaderli
 * @since 2022/8/28
 */
public interface LiEventBusBehavior<T> {

/**
 * @param listener the listener to be removed
 * @see LiEventBus#unRegisterListener(ILiEventListener)
 */
void unRegisterListener(ILiEventListener<T> listener);
}
