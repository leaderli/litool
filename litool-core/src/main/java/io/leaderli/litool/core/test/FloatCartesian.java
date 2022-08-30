package io.leaderli.litool.core.test;

import io.leaderli.litool.core.type.PrimitiveEnum;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class FloatCartesian implements CartesianFunction<FloatValues, Float> {
    @Override
    public Float[] apply(FloatValues floatValues, CartesianContext context) {

        float[] value = floatValues.value();
        if (value.length > 0) {
            return (Float[]) PrimitiveEnum.toWrapperArray(value);
        }
        return CartesianUtil.cartesian(Float.class);
    }
}
