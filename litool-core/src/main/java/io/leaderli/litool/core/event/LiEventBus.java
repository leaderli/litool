package io.leaderli.litool.core.event;

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
 * @see ILiEventListener#onError(Throwable)
 * @see ILiEventListener#after(LiEventBusBehavior)
 */
public <T> void push(T event) {
    if (event == null) {
        return;
    }
    Class<T> eventType = (Class<T>) event.getClass();
    liEventMap.compute(eventType, listener -> {

        if (listener.before(event)) {

            try {

                listener.listen(event);
            } catch (Throwable throwable) {
                listener.onError(throwable);
                return;
            }
            listener.after(this);
        }

    });
}


}
