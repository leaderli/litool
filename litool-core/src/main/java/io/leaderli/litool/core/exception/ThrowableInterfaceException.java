package io.leaderli.litool.core.exception;

import io.leaderli.litool.core.function.ThrowableFunction;

/**
 * A RuntimeException use for
 * {@link  io.leaderli.litool.core.meta.Lira#throwable_map(ThrowableFunction)}
 *
 * @see RuntimeExceptionTransfer
 */
public class ThrowableInterfaceException extends RuntimeException {

    public ThrowableInterfaceException(Throwable throwable) {
        super(throwable);
    }
}
