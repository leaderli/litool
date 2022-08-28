package io.leaderli.litool.core.event;

import java.util.List;

/**
 * Dispatches events to listeners, and provides ways for listeners to register and unregister themselves
 * <p>
 * The LiEventBus allows publish-listen-style communication between components without requiring the
 * components to explicitly register with one another
 * <p>
 * when an event is pushed , all registered listeners listen for this particular event will receive the event
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class LiEventBus implements LiEventBusBehavior {

private final LiEventMap liEventMap = new LiEventMap();


/**
 * @param listener a listener
 */
public void registerListener(ILiEventListener listener) {
    liEventMap.put(listener.componentType(), listener);
}

@Override
public void unRegisterListener(ILiEventListener listener) {
    liEventMap.remove(listener);
}

/**
 * @param event the pushed event
 * @param <T>   the type of pushed event
 * @see ILiEventListener
 * @see ILiEventListener#before(Object)
 * @see ILiEventListener#listen(Object)
 * @see ILiEventListener#after(LiEventBusBehavior)
 */
public <T> void push(T event) {
    if (event == null) {
        return;
    }
    Class<T> cls = (Class<T>) event.getClass();
    List<ILiEventListener<T>> listeners = liEventMap.get(cls);
    for (ILiEventListener<T> listener : listeners) {
        if (listener.before(event)) {
            listener.listen(event);
            listener.after(this);
        }

    }
}


}
