package io.leaderli.litool.core.exception;

/**
 * A RuntimeException use for {@link  RuntimeExceptionTransfer}
 *
 * @see RuntimeExceptionTransfer
 */
public class ThrowableInterfaceException extends RuntimeException {

    public ThrowableInterfaceException(Throwable throwable) {
        super(throwable);
    }
}
