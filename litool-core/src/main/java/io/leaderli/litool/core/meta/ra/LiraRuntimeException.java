package io.leaderli.litool.core.meta.ra;

/**
 * a runtimeException specific used for lira, that can interrupt lira chain
 *
 * @author leaderli
 * @see IterableRa
 * @since 2022/9/27 2:20 PM
 */
public class LiraRuntimeException extends RuntimeException {
//    /**
//     *
//     */
//    public LiraRuntimeException() {
//        super();
//    }
//
//    /**
//     * @param message 异常信息
//     */
//    public LiraRuntimeException(String message) {
//        super(message);
//    }
//
//    /**
//     * @param message 异常信息
//     * @param cause   异常原因
//     */
//    public LiraRuntimeException(String message, Throwable cause) {
//        super(message, cause);
//    }

    /**
     * @param cause 异常原因
     */
    public LiraRuntimeException(Throwable cause) {
        super(cause);
    }


}
