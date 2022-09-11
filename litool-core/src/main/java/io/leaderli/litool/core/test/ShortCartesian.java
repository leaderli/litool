package io.leaderli.litool.core.test;

import io.leaderli.litool.core.collection.CollectionUtils;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class ShortCartesian implements CartesianFunction<ShortValues, Short> {
    @Override
    public Short[] apply(ShortValues shortValues, CartesianContext context) {

        short[] value = shortValues.value();
        if (value.length > 0) {
            return (Short[]) CollectionUtils.toWrapperArray(value);
        }
        return CartesianUtil.cartesian_single_def(Short.class);
    }
}
