package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.util.BooleanUtil;

import java.util.function.Function;

/**
 * 过滤不符合断言的元素
 *
 * @author leaderli
 * @see Lino#filter(Function)
 * @see BooleanUtil#parse(Object)
 * @since 2022/6/27
 */
public class UntilSome<T> extends PublisherSome<T> {


private final Function<? super T, ?> filter;

public UntilSome(Publisher<T> prevPublisher, Function<? super T, ?> filter) {
    super(prevPublisher);
    this.filter = filter;
}

@Override
public void subscribe(Subscriber<? super T> actualSubscriber) {
    prevPublisher.subscribe(new FilterSubscriber(actualSubscriber));

}


private final class FilterSubscriber extends IntermediateSubscriber<T, T> {

    public FilterSubscriber(Subscriber<? super T> actualSubscriber) {
        super(actualSubscriber);
    }

    @Override
    public void next(T t) {
        actualSubscriber.next(t);
        boolean present = BooleanUtil.parse(filter.apply(t));
        if (present) {
            cancel();
        }

    }

    @Override
    public void onNull() {
        //  filter will avoid null element
    }
}
}
