package io.leaderli.litool.core.event;

/**
 * 分发事件给监听器，并提供监听器注册和注销的方法。
 * <p>
 * LiEventBus 可以在组件之间实现发布-订阅式的通信，而不需要组件之间显式地相互注册。
 * <p>
 * 当推送事件时，所有已注册监听该事件的监听器都会接收到该事件。
 */
@SuppressWarnings({"unchecked"})
public class LiEventBus implements LiEventBusBehavior {

    private final LiEventMap liEventMap = new LiEventMap();


    /**
     * 注册监听器。
     *
     * @param listener 要注册的监听器
     */
    public <E extends LiEventObject<S>, S> void registerListener(ILiEventListener<E, S> listener) {
        liEventMap.put(listener.componentType(), listener);
    }

    /**
     * 注销监听器。
     *
     * @param listener 要注销的监听器
     */
    @Override
    public <E extends LiEventObject<S>, S> void unRegisterListener(ILiEventListener<E, S> listener) {
        liEventMap.remove(listener);
    }

    /**
     * 推送事件。
     *
     * @param event 推送的事件
     * @param <S>   推送的事件的数据类型
     * @param <E>   推送的事件的类型
     * @see ILiEventListener
     * @see ILiEventListener#before(Object)
     * @see ILiEventListener#listen(Object)
     * @see ILiEventListener#onError(Throwable)
     * @see ILiEventListener#after(LiEventBusBehavior)
     */
    public <E extends LiEventObject<S>, S> void push(E event) {
        if (event == null) {
            return;
        }
        Class<E> eventType = (Class<E>) event.getClass();
        liEventMap.compute(eventType, listener -> {

            S source = event.getSource();
            if (source == null) {
                listener.onNull();
                return;
            }
            if (listener.before(source)) {

                try {

                    listener.listen(source);
                } catch (Throwable throwable) {
                    listener.onError(throwable);
                    return;
                }
                listener.after(this);
            }

        });
    }


}
