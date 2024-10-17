package io.leaderli.litool.core.event;

public interface LiEventListenerConsumer<E extends LiEventObject<S>, S> {
    void accpt(S source, LiEventBusBehavior liEventBusBehavior, ILiEventListener<E, S> listener);
}
