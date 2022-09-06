package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/7/18
 */
abstract class PublisherRa<T> extends Ra<T> {

    protected final Publisher<T> prevPublisher;

    protected PublisherRa(Publisher<T> prevPublisher) {
        this.prevPublisher = prevPublisher;
    }
}
