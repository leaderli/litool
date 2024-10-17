package io.leaderli.litool.core.event;

import java.util.function.BiConsumer;

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
     * @param <E>      事件类型
     * @param <S>      消息类型
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
        push(event, DEFAULT);
    }

    /**
     * 推送事件。
     *
     * @param event 推送的事件
     * @param sl    提供源数据和监听器的执行过程函数，默认函数 {@link  #DEFAULT}
     * @param <S>   推送的事件的数据类型
     * @param <E>   推送的事件的类型
     * @see ILiEventListener
     * @see ILiEventListener#before(Object)
     * @see ILiEventListener#listen(Object)
     * @see ILiEventListener#onError(Throwable)
     * @see ILiEventListener#after(LiEventBusBehavior)
     */
    public <E extends LiEventObject<S>, S> void push(E event, BiConsumer<SourceProvider<S>, ILiEventListener<E, S>> sl) {

        if (event == null) {
            return;
        }
        Class<E> eventType = (Class<E>) event.getClass();
        SourceProvider<S> sourceProvider = new SourceProvider<>(event.getSource(), this);
        liEventMap.compute(eventType, listener -> sl.accept(sourceProvider, listener));
    }


    @SuppressWarnings("rawtypes")
    private static final DefaultBiConsumer DEFAULT = new DefaultBiConsumer();

    private static class DefaultBiConsumer<E extends LiEventObject<S>, S> implements BiConsumer<SourceProvider<S>, ILiEventListener<E, S>> {

        @Override
        public void accept(SourceProvider<S> sourceProvider, ILiEventListener<E, S> listener) {
            S source = sourceProvider.source;
            if (source == null) {
                listener.onNull();
                return;
            }
            try {
                if (listener.before(source)) {
                    listener.listen(source);
                    listener.after(sourceProvider.liEventBusBehavior);
                }
            } catch (Throwable throwable) {
                listener.onError(throwable);
            }
        }
    }


}
