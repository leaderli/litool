package io.leaderli.litool.core.event;

import java.util.List;

/**
 * 用来注册 与卸载 {@link ILiEventListener}
 * <p>
 * 当推送事件时 {@link #push(LiEventObject)} , 所有注册的监听器都会收到该事件 {@link ILiEventListener#listen(Object)}
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class LiEventBus {

private final LiEventMap liEventMap = new LiEventMap();


public void registerListener(ILiEventListener listener) {
    //noinspection unchecked
    liEventMap.put(listener.componentType(), listener);
}

public void unRegisterListener(ILiEventListener listener) {
    liEventMap.remove(listener);
}

/**
 * 推送事件，触发 {@link ILiEventListener#listen(Object)} ,
 * 当 {@link ILiEventListener#removeIf()} 为 true 时， 移除监听器
 *
 * @param liEvent 推送的事件
 * @param <T>     推送事件的泛型
 * @see ILiEventListener#listen(Object)
 * @see ILiEventListener#removeIf()
 */
public <T extends LiEventObject> void push(T liEvent) {
    if (liEvent == null) {
        return;
    }
    Class<T> cls = (Class<T>) liEvent.getClass();
    List<ILiEventListener<T>> listeners = liEventMap.get(cls);
    listeners.forEach(listener -> {
        listener.listen(liEvent);
        if (listener.removeIf()) {
            liEventMap.remove(listener);
        }
    });
}

}
