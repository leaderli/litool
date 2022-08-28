package io.leaderli.litool.core.function;

/**
 * Represents an operation that accepts a single input Throwable
 *
 * @author leaderli
 * @see io.leaderli.litool.core.event.ILiEventListener
 * @since 2022/8/28
 */
@FunctionalInterface
public interface OnError {

/**
 * Perform this operation on the given throwable
 *
 * @param throwable the input throwable
 */
void onError(Throwable throwable);

}
