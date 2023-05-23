package io.leaderli.litool.core.test;

import io.leaderli.litool.core.collection.ArrayUtils;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class LongCartesian implements CartesianFunction<LongValues, Long> {
    @Override
    public Long[] apply(LongValues annotatedByValuable, CartesianContext context) {
        long[] value = annotatedByValuable.value();
        if (value.length > 0) {
            return ArrayUtils.toArray(value);
        }
        return CartesianUtil.cartesian_single_def(Long.class);
    }
}
