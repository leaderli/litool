package io.leaderli.litool.core.condition;

import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.Lino;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/6/23
 */
public interface LiIf<T, R> extends IfPublisher<T, R> {


    static <T, R> LiIf<T, R> of(T value) {
        return of(Lino.of(value));
    }

    static <T, R> LiIf<T, R> of(Lino<T> lino) {
        if (lino == null) {
            lino = Lino.none();
        }
        return new Begin<>(lino);
    }

    default LiThen<T, R> _if(Function<T, Object> filter) {

        return new When<>(this, filter);
    }

    default Lino<R> _else(Supplier<R> supplier) {
        Other<T, R> other = new Other<>(this, supplier);
        End<T, R> end = new End<>();
        return end.request(other);
    }

    class When<T, R> implements LiThen<T, R> {
        private final IfPublisher<T, R> prevPublisher;
        private final Function<T, Object> filter;

        public When(IfPublisher<T, R> prevPublisher, Function<T, Object> filter) {
            this.prevPublisher = prevPublisher;
            this.filter = filter;
        }

        @Override
        public void subscribe(IfSubscriber<T, R> actualSubscriber) {
            prevPublisher.subscribe(new WhenSubscriber<>(filter, actualSubscriber));

        }


    }

    class WhenSubscriber<T, R> extends IfMiddleSubscriber<T, R> {
        private final Function<T, Object> filter;

        public WhenSubscriber(Function<T, Object> filter, IfSubscriber<T, R> actualSubscriber) {
            super(actualSubscriber);
            this.filter = filter;

        }


        @Override
        public void next(T t, Function<T, Object> predicate) {

            this.actualSubscriber.next(t, this.filter);
        }

    }

    class Other<T, R> implements LiThen<T, R> {
        private final IfPublisher<T, R> prevPublisher;
        private final Supplier<R> supplier;

        public Other(IfPublisher<T, R> prevPublisher, Supplier<R> supplier) {
            this.prevPublisher = prevPublisher;
            this.supplier = supplier;
        }

        @Override
        public void subscribe(IfSubscriber<T, R> actualSubscriber) {
            prevPublisher.subscribe(new OtherSubscriber<>(supplier, actualSubscriber));

        }
    }

    class OtherSubscriber<T, R> extends IfMiddleSubscriber<T, R> {
        private final Supplier<R> supplier;

        public OtherSubscriber(Supplier<R> supplier, IfSubscriber<T, R> actualSubscriber) {
            super(actualSubscriber);
            this.supplier = supplier;

        }


        @Override
        public void next(T t, Function<T, Object> predicate) {
            onComplete(supplier.get());
        }

    }

    class Begin<T, R> implements LiIf<T, R> {
        private final Lino<T> lino;

        public Begin(Lino<T> lino) {
            this.lino = lino;
        }

        @Override
        public void subscribe(IfSubscriber<T, R> subscriber) {

            subscriber.onSubscribe(new IfSubscription<R>() {


                private LiBox<R> box;

                @Override
                public void request(LiBox<R> box) {

                    this.box = box;
                    subscriber.next(lino.get(), null);
                }

                @Override
                public void onComplete(R value) {

                    this.box.value(value);
                }
            });
        }

    }

    class End<T, R> implements IfSubscriber<T, R> {

        private final LiBox<R> box = LiBox.none();

        private End() {
        }

        @Override
        public void onSubscribe(IfSubscription<R> subscription) {

            subscription.request(box);
        }

        @Override
        public void next(T t, Function<T, Object> predicate) {

            throw new UnsupportedOperationException();
        }

        public Lino<R> request(Other<T, R> other) {
            other.subscribe(this);
            return box.lino();
        }
    }
}

