package io.leaderli.litool.core.exception;

import io.leaderli.litool.core.function.Function;
import io.leaderli.litool.core.meta.Lira;

/**
 * A RuntimeException use for
 * {@link  Lira#throwable_map(Function)}
 */
public class ThrowableInterfaceException extends RuntimeException {

    public ThrowableInterfaceException(Throwable throwable) {
        super(throwable);
    }
}
