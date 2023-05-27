package io.leaderli.litool.core.exception;

import io.leaderli.litool.core.type.TypeUtil;
import io.leaderli.litool.core.util.ObjectsUtil;

import java.lang.reflect.Type;

/**
 * @author leaderli
 * @see TypeUtil
 * @see ObjectsUtil
 * @since 2022/8/13 9:34 AM
 */
public class UnsupportedTypeException extends RuntimeException {
    public UnsupportedTypeException(Type type) {

        super(type.getClass() + ":" + type.getTypeName());
    }

}
