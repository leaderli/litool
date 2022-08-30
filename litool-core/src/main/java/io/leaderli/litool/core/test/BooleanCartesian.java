package io.leaderli.litool.core.test;

import io.leaderli.litool.core.type.PrimitiveEnum;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class BooleanCartesian implements CartesianFunction<BooleanValues, Boolean> {
    @Override
    public Boolean[] apply(BooleanValues booleanValues, CartesianContext context) {
        boolean[] value = booleanValues.value();
        if (value.length > 0) {
            return (Boolean[]) PrimitiveEnum.toWrapperArray(value);
        }
        return CartesianUtil.cartesian(Boolean.class);

    }
}
