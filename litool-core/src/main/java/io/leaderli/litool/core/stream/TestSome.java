package io.leaderli.litool.core.stream;

import io.leaderli.litool.core.util.BooleanUtil;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/9/12
 */
public class TestSome<T> extends Some<T> {
    private final Function<T, ?> predicate;

    public TestSome(PublisherLogic<T> prePublisher, Function<T, ?> predicate) {
        super(prePublisher);
        this.predicate = predicate;
    }

    @Override
    public void subscribe(SubscriberLogic<T> actualSubscriber) {
        prevPublisher.subscribe(new TestSubscriberSubscription(actualSubscriber));

    }

    private class TestSubscriberSubscription extends IntermediateSubscriberSubscription<T> {


        private boolean negation;

        protected TestSubscriberSubscription(SubscriberLogic<T> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T t, boolean last) {

            boolean test = BooleanUtil.parse(predicate.apply(t));
            if (negation) {
                test = BooleanUtil.negate(test);
            }
            super.next(t, test);
        }

        @Override
        public void onNot() {
            negation = true;
        }
    }

}
