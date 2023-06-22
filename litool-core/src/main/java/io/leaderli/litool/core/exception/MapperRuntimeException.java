package io.leaderli.litool.core.exception;

import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.meta.Lira;

/**
 * {@link  Lira#mapIgnoreError(ThrowableFunction)}
 */
public class MapperRuntimeException extends RuntimeException {

    /**
     * @param throwable 异常
     */
    public MapperRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
