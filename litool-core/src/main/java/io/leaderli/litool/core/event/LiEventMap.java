package io.leaderli.litool.core.event;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ComponentType;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 事件监听器的类型与监听器本身的映射容器。
 * 在该容器中，事件监听器的泛型类型与对应的事件类型相同。
 * <p>
 * 注意：该容器不是线程安全的。
 */

class LiEventMap {

    private final Map<Class<?>, Set<ILiEventListener<?, LiEventObject<?>>>> eventListenerMap = new HashMap<>();

    /**
     * 向容器中添加事件类型与对应的监听器。
     *
     * @param eventType 事件类型
     * @param listener  监听器
     * @param <S>       事件的数据类型
     * @param <E>       事件类型与监听器的泛型类型
     */
    @SuppressWarnings("unchecked")
    public <E extends LiEventObject<S>, S> void put(Class<E> eventType, ILiEventListener<E, S> listener) {

        this.eventListenerMap.computeIfAbsent(eventType, c -> new LinkedHashSet<>()).add((ILiEventListener<?, LiEventObject<?>>) listener);
    }


    /**
     * 对与指定事件类型对应的监听器执行指定的操作。
     *
     * @param eventType 事件类型
     * @param consumer  对监听器执行的操作
     * @param <S>       事件的数据类型
     * @param <E>       事件类型与监听器的泛型类型
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <E extends LiEventObject<S>, S> void compute(Class<E> eventType, Consumer<ILiEventListener<E, S>> consumer) {

        if (consumer == null || eventType == null) {
            return;
        }

        // 由于监听器可能在consumer中自行移除，因此不能直接对List执行forEach操作。
        ILiEventListener[] listeners = Lira.of(this.eventListenerMap.get(eventType))
                .toArray(ILiEventListener.class);
        for (ILiEventListener<E, S> listener : listeners) {
            consumer.accept(listener);
        }
    }


    /**
     * 从{@link #eventListenerMap}中移除指定的监听器，如果指定的事件类型对应的监听器列表为空时，从Map中移除该事件类型。
     *
     * @param listener 要移除的监听器
     * @param <S>      事件的数据类型
     * @param <E>      事件类型与监听器的泛型类型
     */
    public <E extends LiEventObject<S>, S> void remove(ILiEventListener<E, S> listener) {

        Class<E> eventType = ComponentType.componentType(listener);

        Set<ILiEventListener<?, LiEventObject<?>>> listeners = this.eventListenerMap.get(eventType);

        if (listeners == null) {
            return;
        }
        listeners.removeIf(item -> item == listener);

        // 大概率会继续创建，不选择删除
//        if (listeners.isEmpty()) {
//            this.eventListenerMap.remove(eventType);
//        }
    }


}
