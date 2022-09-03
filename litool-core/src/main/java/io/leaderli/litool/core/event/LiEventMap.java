package io.leaderli.litool.core.event;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ComponentType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A Map container used to store the class type of the listener event and the listener.
 * the generic type of the listener and the event is same.
 * <p>
 * This container is not thread-safe
 */

class LiEventMap {

    private final Map<Class<?>, List<ILiEventListener<?>>> eventListenerMap = new HashMap<>();

    /**
     * Putting the event type and corresponding listener
     *
     * @param eventType the type of event
     * @param listener  the corresponding listener that listen for the event
     * @param <T>       the type of event and the corresponding listener componentType
     */
    public <T> void put(Class<T> eventType, ILiEventListener<T> listener) {

        List<ILiEventListener<?>> listeners = this.eventListenerMap.computeIfAbsent(eventType, c -> new ArrayList<>());

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Perform action on the corresponding listeners
     *
     * @param eventType the type of event
     * @param consumer  the action that perform on the corresponding listeners
     * @param <T>       the type of event and the corresponding listener componentType
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> void compute(Class<T> eventType, Consumer<ILiEventListener<T>> consumer) {

        if (consumer == null || eventType == null) {
            return;
        }

        //  because listener may be remove themself in consumer, so it should not directly forEach List
        List<ILiEventListener> lira = Lira.of(this.eventListenerMap.get(eventType))
                .cast(ILiEventListener.class).get();

        for (ILiEventListener<T> listener : lira) {
            consumer.accept(listener);

        }
    }


    /**
     * Remove the listener that store on {@link  #eventListenerMap}, then if the eventType
     * corresponding listeners have all be removed, remove the empty list from the Map
     *
     * @param listener the listener will be removed
     * @param <T>      the type of listener corresponding event
     */
    public <T> void remove(ILiEventListener<T> listener) {

        Class<T> eventType = ComponentType.componentType(listener);

        List<ILiEventListener<?>> listeners = this.eventListenerMap.get(eventType);

        if (listeners == null) {
            return;
        }
        listeners.removeIf(item -> item == listener);

        if (listeners.isEmpty()) {
            this.eventListenerMap.remove(eventType);
        }
    }


}
