package io.leaderli.litool.core.test;

import io.leaderli.litool.core.collection.CollectionUtils;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class ShortCartesian implements CartesianFunction<ShortValues, Short> {
    @Override
    public Short[] apply(ShortValues annotatedByValuable, CartesianContext context) {

        short[] value = annotatedByValuable.value();
        if (value.length > 0) {
            return (Short[]) CollectionUtils.toWrapperArray(value);
        }
        return CartesianUtil.cartesian_single_def(Short.class);
    }
}
