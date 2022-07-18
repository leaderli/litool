package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/7/18
 */
public abstract class DefaultSomeRa<T> extends SomeRa<T> {

    protected final PublisherRa<T> prevPublisher;

    protected DefaultSomeRa(PublisherRa<T> prevPublisher) {
        this.prevPublisher = prevPublisher;
    }
}
