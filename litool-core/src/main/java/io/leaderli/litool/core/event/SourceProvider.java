package io.leaderli.litool.core.event;


public class SourceProvider<S> {
    public final S source;
    public final LiEventBusBehavior liEventBusBehavior;


    public SourceProvider(S source, LiEventBusBehavior liEventBusBehavior) {
        this.source = source;
        this.liEventBusBehavior = liEventBusBehavior;
    }
}
