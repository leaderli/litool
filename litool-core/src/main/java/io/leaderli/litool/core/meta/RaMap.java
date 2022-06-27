package io.leaderli.litool.core.meta;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public class RaMap<T, R> extends RaSome<R> {
    private final Function<? super T, ? extends R> mapper;
    private final RaPublisher<T> prevPublisher;

    public RaMap(RaPublisher<T> prevPublisher, Function<? super T, ? extends R> mapper) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(RaSubscriber<? super R> actualSubscriber) {
        prevPublisher.subscribe(new MapRaSubscriber<>(actualSubscriber, mapper));

    }

    private static class MapRaSubscriber<T, R> extends IntermediateRaSubscriber<T, R> {

        private final Function<? super T, ? extends R> mapper;

        private MapRaSubscriber(RaSubscriber<? super R> actualSubscriber, Function<? super T, ? extends R> mapper) {
            super(actualSubscriber);
            this.mapper = mapper;
        }


        @Override
        public void next(Lino<T> t) {
            t.map(mapper).nest(f -> this.actualSubscriber.next(Lino.narrow(f)));
        }

    }
}
