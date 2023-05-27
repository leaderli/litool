package io.leaderli.litool.core.exception;

/**
 * 用于 {@link  RuntimeExceptionTransfer} 的异常类
 *
 * @see RuntimeExceptionTransfer
 */
public class RuntimeExceptionTransferException extends RuntimeException {

    public RuntimeExceptionTransferException(Throwable throwable) {
        super(throwable);
    }
}
