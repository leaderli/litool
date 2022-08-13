package io.leaderli.litool.core.exception;

/**
 * @author leaderli
 * @since 2022/8/13 9:34 AM
 */
public class UnsupportedClassException extends RuntimeException {
    public UnsupportedClassException(Class<?> cls) {

        super(cls.getName());
    }
}
