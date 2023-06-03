package io.leaderli.litool.core.exception;

import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.meta.Lira;

/**
 * A RuntimeException use for
 * {@link  Lira#throwable_map(ThrowableFunction)}
 */
public class ThrowableInterfaceException extends RuntimeException {

    public ThrowableInterfaceException(Throwable throwable) {
        super(throwable);
    }
}
