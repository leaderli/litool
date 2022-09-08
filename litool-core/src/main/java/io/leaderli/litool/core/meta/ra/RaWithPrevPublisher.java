package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/7/18
 */
abstract class RaWithPrevPublisher<T> extends Ra<T> {

    protected final PublisherRa<T> prevPublisher;

    protected RaWithPrevPublisher(PublisherRa<T> prevPublisher) {
        this.prevPublisher = prevPublisher;
    }
}
