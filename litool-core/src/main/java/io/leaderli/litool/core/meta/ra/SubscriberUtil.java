package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/8/30
 */
public class SubscriberUtil {

    /**
     * <pre>
     *     if (t == null) {
     *         subscriber.next();
     *     } else {
     *         subscriber.next(t);
     *     }
     * </pre>
     *
     * @param subscriber {@link  Subscriber}
     * @param t          the element signaled
     * @param <T>        the type of element signaled.
     */
    public static <T> void next(Subscriber<T> subscriber, T t) {

        if (t == null) {
            subscriber.next_null();
        } else {
            subscriber.next(t);
        }
    }
}
