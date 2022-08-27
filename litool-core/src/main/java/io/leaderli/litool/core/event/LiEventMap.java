package io.leaderli.litool.core.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一个用于存储 监听事件的class 类型 和 监听器 的 Map 容器 ,  监听器 和 事件的泛型是相同的，非线程安全
 */
class LiEventMap {

private final Map<Class<?>, List<ILiEventListener<?>>> eventListenerMap = new HashMap<>();

/**
 * @param cls      {@link LiEventObject} 的 类型
 * @param listener 监听器
 * @param <T>      泛型
 */
public <T extends LiEventObject<?>> void put(Class<T> cls, ILiEventListener<T> listener) {

    List<ILiEventListener<?>> listeners = this.eventListenerMap.computeIfAbsent(cls, c -> new ArrayList<>());

    if (!listeners.contains(listener)) {
        listeners.add(listener);
    }
}

/**
 * @param cls {@link LiEventObject} 的 类型
 * @param <T> 泛型
 * @return 返回指定类型的所有监听器的拷贝
 */
@SuppressWarnings("unchecked")
public <T> List<ILiEventListener<T>> get(Class<T> cls) {
    Object iLiEventListeners = this.eventListenerMap.computeIfAbsent(cls, c -> new ArrayList<>());
    return new ArrayList<>((List<ILiEventListener<T>>) iLiEventListeners);
}


/**
 * 移除指定的监听器，当该监听器监听类型的所有监听器都被移除了，则移除整个类型的集合
 *
 * @param listener 监听器
 * @param <T>      泛型
 */
public <T> void remove(ILiEventListener<T> listener) {

    Class<T> cls = listener.componentType();

    List<ILiEventListener<?>> listeners = this.eventListenerMap.computeIfAbsent(cls, c -> new ArrayList<>());

    listeners.removeIf(item -> item == listener);

    if (listeners.isEmpty()) {
        this.eventListenerMap.remove(cls);
    }
}


}
