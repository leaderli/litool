package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/8/31 12:08 PM
 */
public interface Exceptionable {

    /**
     * invoked when response to notification has throw error, can use {@link CancelSubscription#cancel()} to cancel the
     * demand
     *
     * @param t      the element signaled
     * @param cancel use to cancel demand
     */
    default void onError(Throwable t, CancelSubscription cancel) {
    }

}
