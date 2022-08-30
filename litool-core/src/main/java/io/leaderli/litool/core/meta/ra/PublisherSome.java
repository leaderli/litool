package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/7/18
 */
public abstract class PublisherSome<T> extends Some<T> {

    protected final Publisher<T> prevPublisher;

    protected PublisherSome(Publisher<T> prevPublisher) {
        this.prevPublisher = prevPublisher;
    }
}
