package io.leaderli.litool.core.exception;

public class AutoCloseRuntimeException extends RuntimeException {
    public AutoCloseRuntimeException(Throwable e) {
        super(e);
    }
}
