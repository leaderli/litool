package io.leaderli.litool.core.meta.ra;

/**
 * 跳过前几个元素
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class SkipSome<T> extends PublisherSome<T> {
private final int skip;

public SkipSome(Publisher<T> prevPublisher, int skip) {
    super(prevPublisher);
    this.skip = skip;
}

@Override
public void subscribe(Subscriber<? super T> actualSubscriber) {
    prevPublisher.subscribe(new SkipSubscriber<>(actualSubscriber, skip));

}

private static class SkipSubscriber<T> extends IntermediateSubscriber<T, T> {

    private int skip;

    private SkipSubscriber(Subscriber<? super T> actualSubscriber, int skip) {
        super(actualSubscriber);
        this.skip = skip;
    }


    @Override
    public void next(T t) {
        if (skip < 1) {

            this.actualSubscriber.next(t);
        } else {
            skip--;
        }
    }

}
}
