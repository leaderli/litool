package io.leaderli.litool.core.event;


/**
 * 提供一些有限的方法供监听器使用。
 *
 * @since 2022/8/28
 */
@FunctionalInterface
public interface LiEventBusBehavior {

    /**
     * 注销监听器。
     *
     * @param listener 要注销的监听器
     * @param <E>      事件类型
     * @param <S>      消息类型
     * @see LiEventBus#unRegisterListener(ILiEventListener)
     */
    <E extends LiEventObject<S>, S> void unRegisterListener(ILiEventListener<E, S> listener);
}
