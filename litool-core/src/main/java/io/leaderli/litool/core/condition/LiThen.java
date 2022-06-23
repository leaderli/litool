package io.leaderli.litool.core.condition;

import io.leaderli.litool.core.util.LiBoolUtil;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/6/23
 */
public interface LiThen<T, R> extends IfPublisher<T, R> {


    default LiIf<T, R> then(Function<T, R> mapping) {

        return new Then<>(this, mapping);
    }

    class Then<T, R> implements LiIf<T, R> {

        private final IfPublisher<T, R> prevPublisher;
        private final Function<T, R> mapper;


        public Then(IfPublisher<T, R> prevPublisher, Function<T, R> mapper) {
            this.prevPublisher = prevPublisher;
            this.mapper = mapper;
        }


        public void subscribe(IfSubscriber<T, R> actualSubscriber) {
            prevPublisher.subscribe(new ThenSubscriber<>(mapper, actualSubscriber));

        }


    }

    class ThenSubscriber<T, R> extends IfMiddleSubscriber<T, R> {
        private final Function<T, R> mapper;

        public ThenSubscriber(Function<T, R> mapper, IfSubscriber<T, R> actualSubscriber) {
            super(actualSubscriber);
            this.mapper = mapper;

        }


        @Override
        public void next(T t, Function<T, Object> predicate) {

            if (t != null && LiBoolUtil.parse(predicate.apply(t))) {
                this.onComplete(this.mapper.apply(t));
            } else {
                this.actualSubscriber.next(t, null);
            }
        }

    }

}
