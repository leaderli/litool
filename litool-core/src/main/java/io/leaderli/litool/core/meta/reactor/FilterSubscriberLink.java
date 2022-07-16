package io.leaderli.litool.core.meta.reactor;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.util.LiBoolUtil;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/16
 */
class FilterSubscriberLink<T> extends IntermediateSubscriberLink<T> {
    private final Function<? super T, Object> filter;

    public FilterSubscriberLink(SubscriberLink<T> actualSubscriber, Function<? super T, Object> filter) {
        super(actualSubscriber);
        this.filter = filter;
    }

    @Override
    public void next(T value) {

        boolean next = LiBoolUtil.parse(filter.apply(value));


        if (next) {
            this.actualSubscriber.next(value);
        } else {
            this.actualSubscriber.onCancel(Lino.of(value));
        }
    }
}
