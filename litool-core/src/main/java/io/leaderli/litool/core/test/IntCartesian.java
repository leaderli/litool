package io.leaderli.litool.core.test;

import io.leaderli.litool.core.collection.ArrayUtils;

/**
 * @author leaderli
 * @since 2022/8/20
 */
class IntCartesian implements CartesianFunction<IntValues, Integer> {
    @Override
    public Integer[] apply(IntValues annotatedByValuable, CartesianContext context) {
        int[] value = annotatedByValuable.value();
        if (value.length > 0) {
            return ArrayUtils.toArray(value);
        }
        return CartesianUtil.cartesian_single_def(Integer.class);
    }
}
