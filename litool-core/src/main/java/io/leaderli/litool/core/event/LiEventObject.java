package io.leaderli.litool.core.event;


/**
 * 事件对象，包含源对象，用于触发特定类型的行为
 *
 * @param <S> 事件源对象类型
 */
public abstract class LiEventObject<S> {


    private final S source;

    public LiEventObject(S source) {
        this.source = source;
    }

    /**
     * 获取事件源对象
     * 建议使用 {@link ILiEventListener} 监听 {@link LiEventBus#push(LiEventObject)} 推送的事件
     *
     * @return 事件源对象
     * @see ILiEventListener#listen(Object)
     * @see ILiEventListener#onNull()
     */
    public final S getSource() {
        return source;
    }


    @Override
    public final String toString() {

        return getClass().getSimpleName() + "[source=" + source + "]";
    }


}
