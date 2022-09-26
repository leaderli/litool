package io.leaderli.litool.core.test;

import io.leaderli.litool.core.collection.CollectionUtils;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class ByteCartesian implements CartesianFunction<ByteValues, Byte> {

    @Override
    public Byte[] apply(ByteValues annotatedByValuable, CartesianContext context) {
        byte[] value = annotatedByValuable.value();
        if (value.length > 0) {
            return (Byte[]) CollectionUtils.toWrapperArray(value);
        }
        return CartesianUtil.cartesian_single_def(Byte.class);
    }
}
