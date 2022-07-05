package io.leaderli.litool.core.stream;


import io.leaderli.litool.core.meta.Lino;

public abstract class LiSink<T, R> implements LiFunction<T, R> {

    public final Lino<LiSink<T, R>> prevSink;
    protected Lino<LiSink<T, R>> nextSink;


    protected LiSink(LiSink<T, R> prev) {
        this.prevSink = Lino.of(prev);
        this.prevSink.ifPresent(sink -> sink.nextSink = Lino.of(this));
        this.nextSink = Lino.none();
    }


    public final R next(T request, R lastValue) {
        return this.nextSink.map(sink -> sink.apply(request, lastValue)).or(lastValue).get();
    }


    public final R request(T request) {

        LiSink<T, R> prev = this;
        while (prev.prevSink.present()) {
            prev = prev.prevSink.get();
        }
        return prev.apply(request, null);
    }

}
