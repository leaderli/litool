package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.Lino;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class MapLink<T, R> extends SomeLink<T, R> {


private final Function<? super T, ? extends R> mapper;


public MapLink(PublisherLink<T> prevPublisher, Function<? super T, ? extends R> mapper) {
    super(prevPublisher);
    this.mapper = mapper;
}

@Override
public void subscribe(SubscriberLink<R> actualSubscriber) {
    prevPublisher.subscribe(new MapSubscriberLink(actualSubscriber));
}


private class MapSubscriberLink extends IntermediateSubscriberLink<T, R> {


    private R newValue;

    protected MapSubscriberLink(SubscriberLink<R> actualSubscriber) {
        super(actualSubscriber);
    }


    /**
     * 当前置节点正确执行时触发该函数，通过对值进行转换，有效的值则会进行下一轮的链条
     *
     * @param value 上一个节点传递的值
     * @see #request(Object)
     * @see #mapper
     */
    @Override
    public void next(T value) {

        Lino.of(newValue)
                .or(mapper.apply(value))
                .ifPresent(actualSubscriber::next)
                .ifAbsent(() -> actualSubscriber.onCancel(Lino.none()));

    }

    @Override
    public void onCancel(Lino<T> value) {
        this.actualSubscriber.onCancel(Lino.of(newValue).or(value.map(mapper)));
    }

    @Override
    public void request(R newValue) {
        this.newValue = newValue;
        this.prevSubscription.request();
    }
}
}
