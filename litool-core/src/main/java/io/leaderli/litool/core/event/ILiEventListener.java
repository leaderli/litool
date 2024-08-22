package io.leaderli.litool.core.event;


import io.leaderli.litool.core.function.OnError;
import io.leaderli.litool.core.type.ComponentType;
import io.leaderli.litool.core.type.TypeUtil;

/**
 * 事件监听器，用于监听特定的事件。当 {@link LiEventBus#push(LiEventObject)} 推送事件时，
 * 可以监听该事件的监听器会接收到该事件。
 *
 * <ul>
 *     <li>1. 调用 {@link #before(Object)} 方法检查是否应该调用 {@link #listen(Object)}</li>
 *     <li>2. 调用 {@link #listen(Object)}</li>
 *     <li>3. 当 {@link #listen(Object)} 抛出 Throwable 时，调用 {@link #onError(Throwable)}</li>
 *     <li>4. {@link #listen(Object)} 执行成功后调用 {@link #after(LiEventBusBehavior)}。可以使用 {@link LiEventBus#unRegisterListener(ILiEventListener)} 方法注销自己。</li>
 * </ul>
 *
 * @param <E> 监听的事件类型
 * @see LiEventBus
 */
@FunctionalInterface
public interface ILiEventListener<E extends LiEventObject<S>, S> extends ComponentType<E>, OnError {

    /**
     * 监听事件。当 {@link LiEventBus#push(LiEventObject)} 推送事件时，可以监听该事件的监听器会接收到该事件。
     *
     * @param source 监听的事件的数据
     * @see LiEventBus#push(LiEventObject)
     */
    void listen(S source);


    /**
     * 返回是否应该调用 {@link #listen(Object)} 方法。
     *
     * @param source 监听的事件的数据
     * @return 是否应该调用 {@link #listen(Object)} 方法
     */
    default boolean before(S source) {
        return true;
    }


    /**
     * 在 {@link #listen(Object)} 方法执行成功后调用。
     *
     * @param eventBusBehavior 提供一些限制方法供监听器使用
     * @see #before(Object)
     * @see #listen(Object)
     */
    @SuppressWarnings("java:S1874")
    default void after(LiEventBusBehavior eventBusBehavior) {

        if (shouldRemove()) {
            eventBusBehavior.unRegisterListener(this);
        }
    }


    /**
     * 是否在监听后卸载监听器。
     *
     * @return 是否在监听后卸载监听器
     * @see LiEventBus#unRegisterListener(ILiEventListener)
     * @see LiEventBus#push(LiEventObject)
     * @deprecated
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    default boolean shouldRemove() {
        return false;
    }


    /**
     * 当 {@link #listen(Object)} 发生错误时执行的操作。
     *
     * @param throwable {@link #listen(Object)} 抛出的异常
     * @see LiEventBus#push(LiEventObject)
     */
    @Override
    default void onError(Throwable throwable) {
        OnError.PRINT_STACK.onError(throwable);
    }

    /**
     * 当监听的时间的数据为null调用
     */
    default void onNull() {

    }

    /**
     * 获取监听的事件类型。
     *
     * @return 监听的事件类型
     */
    @SuppressWarnings("unchecked")
    @Override
    default Class<E> componentType() {
        return (Class<E>) TypeUtil.resolve2Parameterized(getClass(), ILiEventListener.class).getActualClassArgument().get();
    }


}
