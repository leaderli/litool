package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/8/31 12:08 PM
 */
public interface IgnoredExceptionable extends Exceptionable {

    default void onError(Throwable t, CancelSubscription cancel) {
        t.printStackTrace();
    }

}
