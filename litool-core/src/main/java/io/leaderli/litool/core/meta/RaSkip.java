package io.leaderli.litool.core.meta;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public class RaSkip<T, R> extends RaSome<T> {
    private final int skip;
    private final RaPublisher<T> prevPublisher;

    public RaSkip(RaPublisher<T> prevPublisher, int skip) {
        this.prevPublisher = prevPublisher;
        this.skip = skip;
    }

    @Override
    public void subscribe(RaSubscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new SkipRaSubscriber<>(actualSubscriber, skip));

    }

    private static class SkipRaSubscriber<T> extends IntermediateRaSubscriber<T, T> {

        private int skip;

        private SkipRaSubscriber(RaSubscriber<? super T> actualSubscriber, int skip) {
            super(actualSubscriber);
            this.skip = skip;
        }


        @Override
        public void next(Lino<T> t) {
            if (skip < 1) {

                this.actualSubscriber.next(Lino.narrow(t));
            } else {
                skip--;
            }
        }

    }
}
