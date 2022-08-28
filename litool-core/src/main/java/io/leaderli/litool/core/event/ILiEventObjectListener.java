package io.leaderli.litool.core.event;

/**
 * Listener to listen the {@link  LiEventObject} event
 *
 * @param <T> the type of listened event
 * @param <R> the type of  {@link  LiEventObject#getSource()}
 * @author leaderli
 * @since 2022/8/28
 */
public abstract class ILiEventObjectListener<T extends LiEventObject<R>, R> implements ILiEventListener<T> {
/**
 * Delegate to {@link #receive(Object)} use parameter provided by {@link  LiEventObject#getSource()}
 *
 * @param event the listened event
 */
@Override
public final void listen(T event) {

    receive(event.getSource().get());
}

/**
 * Response to the pushed {@link  LiEventObject#getSource()}
 *
 * @param source the source provided by listened {@link  LiEventObject}
 */
abstract void receive(R source);

/**
 * Delegate to {@link  #beforeReceive(Object)} use non-null-parameter provided by {@link  LiEventObject#getSource()}
 *
 * @param event the listened event
 * @return whether the {@link #listen(LiEventObject)} method should be called
 */
@Override
public final boolean before(T event) {

    return event.getSource().map(this::beforeReceive).get(false);
}

/**
 * Return whether the {@link #listen(LiEventObject)} method should be called
 *
 * @param source the source provided by listened {@link  LiEventObject}
 * @return whether the {@link #listen(LiEventObject)} method should be called
 */
public boolean beforeReceive(R source) {
    return true;
}
}
