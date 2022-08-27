package io.leaderli.litool.core.event;


import io.leaderli.litool.core.type.ComponentType;
import io.leaderli.litool.core.type.ReflectUtil;

/**
 * 用来监听事件的监听器
 *
 * @param <T> the type parameter of  LiEventObject componentType
 * @see LiEventBus
 */
public interface ILiEventListener<T> extends ComponentType<T> {

/**
 * @param source 事件中包含的数据对象
 * @see LiEventBus#push(LiEventObject)
 */
void listen(T source);

/**
 * @return 是否在监听后卸载监听器
 * @see LiEventBus#unRegisterListener(ILiEventListener)
 * @see LiEventBus#push(LiEventObject)
 */
default boolean removeIf() {
    return false;
}

@SuppressWarnings("unchecked")
@Override
default Class<T> componentType() {
    return (Class<T>) ReflectUtil.getGenericInterfacesType(getClass(), ILiEventListener.class).get();
}


}
