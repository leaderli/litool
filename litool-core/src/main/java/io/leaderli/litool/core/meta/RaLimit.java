package io.leaderli.litool.core.meta;

/**
 * 限制元素的个数
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class RaLimit<T, R> extends RaSome<T> {
    private final int skip;
    private final RaPublisher<T> prevPublisher;

    public RaLimit(RaPublisher<T> prevPublisher, int skip) {
        this.prevPublisher = prevPublisher;
        this.skip = skip;
    }

    @Override
    public void subscribe(RaSubscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new LimitRaSubscriber<>(actualSubscriber, skip));

    }

    private static class LimitRaSubscriber<T> extends IntermediateRaSubscriber<T, T> {

        private int skip;

        private LimitRaSubscriber(RaSubscriber<? super T> actualSubscriber, int skip) {
            super(actualSubscriber);
            this.skip = skip;
        }


        @Override
        public void next(Lino<T> t) {
            if (skip < 1) {
                this.cancel();

            } else {
                this.actualSubscriber.next(Lino.narrow(t));
                skip--;
            }
        }

    }
}
