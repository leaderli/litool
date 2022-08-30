package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/6/29
 * It is used to cancel demand
 */
public interface CancelSubscription {
    /**
     * Request the {@link Publisher} to stop sending data
     */
    void cancel();
}
