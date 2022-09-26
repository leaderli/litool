package io.leaderli.litool.core.test;

import io.leaderli.litool.core.collection.CollectionUtils;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class FloatCartesian implements CartesianFunction<FloatValues, Float> {
    @Override
    public Float[] apply(FloatValues annotatedByValuable, CartesianContext context) {

        float[] value = annotatedByValuable.value();
        if (value.length > 0) {
            return (Float[]) CollectionUtils.toWrapperArray(value);
        }
        return CartesianUtil.cartesian_single_def(Float.class);
    }
}
