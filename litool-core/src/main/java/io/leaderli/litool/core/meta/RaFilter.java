package io.leaderli.litool.core.meta;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public class RaFilter<T> extends RaSome<T> {
    private final RaPublisher<T> prevPublisher;


    private final Function<? super T, Object> filter;

    public RaFilter(RaPublisher<T> prevPublisher, Function<? super T, Object> filter) {
        this.prevPublisher = prevPublisher;
        this.filter = filter;
    }

    @Override
    public void subscribe(RaSubscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new FilterRaSubscriber<>(actualSubscriber, filter));

    }

    private static class FilterRaSubscriber<T> extends IntermediateRaSubscriber<T, T> {
        private final Function<? super T, Object> filter;

        public FilterRaSubscriber(RaSubscriber<? super T> actualSubscriber, Function<? super T, Object> filter) {
            super(actualSubscriber);
            this.filter = filter;
        }

        @Override
        public void next(Lino<T> t) {

            t.filter(filter).nest(f -> this.actualSubscriber.next(Lino.narrow(f)));

        }

    }
}
