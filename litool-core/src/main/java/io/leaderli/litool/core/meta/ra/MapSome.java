package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.Lino;

import java.util.function.Function;

/**
 * 将元素类型转换为另外一个类型
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class
MapSome<T, R> extends Some<R> {
private final Function<? super T, ? extends R> mapper;
private final Publisher<T> prevPublisher;

public MapSome(Publisher<T> prevPublisher, Function<? super T, ? extends R> mapper) {
    this.prevPublisher = prevPublisher;
    this.mapper = mapper;
}

@Override
public void subscribe(Subscriber<? super R> actualSubscriber) {
    prevPublisher.subscribe(new MapSubscriber(actualSubscriber));

}

private class MapSubscriber extends IntermediateSubscriber<T, R> {


    private MapSubscriber(Subscriber<? super R> actualSubscriber) {
        super(actualSubscriber);
    }


    @Override
    public void next(T t) {
        Lino.of(t).map(mapper).ifPresent(this.actualSubscriber::next);
    }

}
}
