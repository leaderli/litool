package io.leaderli.litool.core.exception;

/**
 * A RuntimeException use for {@link  RuntimeExceptionTransfer}
 *
 * @see RuntimeExceptionTransfer
 */
public class RuntimeExceptionTransferException extends RuntimeException {

    public RuntimeExceptionTransferException(Throwable throwable) {
        super(throwable);
    }
}
