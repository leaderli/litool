package io.leaderli.litool.core.test;

import io.leaderli.litool.core.type.PrimitiveEnum;

/**
 * @author leaderli
 * @since 2022/8/20
 */
class IntCartesian implements CartesianFunction<IntValues, Integer> {
    @Override
    public Integer[] apply(IntValues intValues, CartesianContext context) {
        int[] value = intValues.value();
        if (value.length > 0) {
            return (Integer[]) PrimitiveEnum.toWrapperArray(value);
        }
        return CartesianUtil.cartesian_single_def(Integer.class);
    }
}
