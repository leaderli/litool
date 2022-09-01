package io.leaderli.litool.core.meta.ra;

import java.util.Iterator;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/18
 */
public class FlatMap<T, R> extends Some<R> {


    private final Function<? super T, Iterator<? extends R>> mapper;
    private final Publisher<T> prevPublisher;

    public FlatMap(Publisher<T> prevPublisher, Function<? super T, Iterator<? extends R>> mapper) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(Subscriber<? super R> actualSubscriber) {
        prevPublisher.subscribe(new FlatMapSubscriber(actualSubscriber));

    }


    class FlatMapSubscriber extends BarricadeIntermediateSubscription<T, R> {


        private FlatMapSubscriber(Subscriber<? super R> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T t) {

            Iterator<? extends R> iterator = mapper.apply(t);

            if (iterator != null) {
                barricadeSubscription = new BarricadeSubscription<>(this, actualSubscriber, iterator);
                barricadeSubscription.request();
            }
        }



    }
}
