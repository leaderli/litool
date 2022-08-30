package io.leaderli.litool.core.exception;

import java.lang.reflect.Type;

/**
 * @author leaderli
 * @see io.leaderli.litool.core.type.TypeUtil
 * @see io.leaderli.litool.core.util.ObjectsUtil
 * @since 2022/8/13 9:34 AM
 */
public class UnsupportedTypeException extends RuntimeException {
    public UnsupportedTypeException(Type type) {

        super(type.getClass() + ":" + type.getTypeName());
    }

}
