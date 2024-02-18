package io.leaderli.litool.test.cartesian;

import io.leaderli.litool.core.collection.ArrayUtils;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class BooleanCartesian implements CartesianFunction<BooleanValues, Boolean> {
    @Override
    public Boolean[] apply(BooleanValues annotatedByValuable, CartesianContext context) {
        boolean[] value = annotatedByValuable.value();
        if (value.length > 0) {
            return ArrayUtils.toArray(value);
        }
        return CartesianUtil.cartesian_single_def(Boolean.class);

    }
}
