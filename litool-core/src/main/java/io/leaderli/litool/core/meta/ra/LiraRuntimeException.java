package io.leaderli.litool.core.meta.ra;

/**
 * a runtimeException specific used for lira, that can interrupt lira chain
 *
 * @author leaderli
 * @see IterableRa
 * @since 2022/9/27 2:20 PM
 */
public class LiraRuntimeException extends RuntimeException {
    public LiraRuntimeException() {
        super();
    }

    public LiraRuntimeException(String message) {
        super(message);
    }

    public LiraRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LiraRuntimeException(Throwable cause) {
        super(cause);
    }


    public LiraRuntimeException(LiraRuntimeException cause) {
        super(cause.getMessage(), cause.getCause());
    }
}
