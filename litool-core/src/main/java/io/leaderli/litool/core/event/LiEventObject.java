package io.leaderli.litool.core.event;


/**
 * A event with source , used to trigger specific
 * behavior on the particular type
 */
public abstract class LiEventObject<S> {


    private final S source;

    public LiEventObject(S source) {
        this.source = source;
    }

    /**
     * used to avoid null pointer because some source may be null
     * suggest use {@link  ILiEventListener} to listen the event pushed by {@link LiEventBus#push(LiEventObject)}
     *
     * @return a lino wrapper
     */
    public final S getSource() {
        return source;
    }


    @Override
    public final String toString() {

        return getClass().getSimpleName() + "[source=" + source + "]";
    }


}
