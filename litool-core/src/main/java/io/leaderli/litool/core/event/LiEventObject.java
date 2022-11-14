package io.leaderli.litool.core.event;


import io.leaderli.litool.core.meta.Lino;

/**
 * A event with source , used to trigger specific
 * behavior on the particular type
 */
public class LiEventObject<T> {


    private final T source;

    public LiEventObject(T source) {
        this.source = source;
    }

    /**
     * used to avoid null pointer because some source may be null
     * suggest use {@link  ILiEventObjectListener} to listen the event pushed by {@link LiEventBus#push(Object)}
     *
     * @return a lino wrapper
     */
    public final Lino<T> getSource() {
        return Lino.of(source);
    }


    @Override
    public final String toString() {

        return getClass().getSimpleName() + "[source=" + source + "]";
    }

}
