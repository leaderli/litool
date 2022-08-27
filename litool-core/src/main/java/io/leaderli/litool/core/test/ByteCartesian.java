package io.leaderli.litool.core.test;

import io.leaderli.litool.core.type.PrimitiveEnum;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class ByteCartesian implements CartesianFunction<ByteValues, Byte> {

@Override
public Byte[] apply(ByteValues byteValues, CartesianContext context) {
    byte[] value = byteValues.value();
    if (value.length > 0) {
        return (Byte[]) PrimitiveEnum.toWrapperArray(value);
    }
    return CartesianUtil.cartesian(Byte.class);
}
}
