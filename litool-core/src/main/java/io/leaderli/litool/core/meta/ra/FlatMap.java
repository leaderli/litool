package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.Lino;

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

    private class FlatMapSubscriber extends IntermediateSubscriber<T, R> {


        @Override
        public void request() {
            super.request();
        }

        private FlatMapSubscriber(Subscriber<? super R> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T t) {

            // 展开迭代器，依次对非空元素执行下一步操作
            Lino.of(t).map(mapper).ifPresent(it -> it.forEachRemaining(this.actualSubscriber::next));
//            Iterator<? extends R> apply = mapper.apply(t);
//
//            if (apply != null) {
//                FlatArraySome<R> ts = new FlatArraySome<>(apply);
//
//                ts.subscribe(actualSubscriber);
//            }


        }

        @Override
        public void onNull() {

            // flat null  will return empty
        }

    }
}
