package io.leaderli.litool.test.cartesian;

import io.leaderli.litool.core.collection.ArrayUtils;

/**
 * @author leaderli
 * @since 2022/8/20
 */
class CharCartesian implements CartesianFunction<CharValues, Character> {
    @Override
    public Character[] apply(CharValues annotatedByValuable, CartesianContext context) {
        char[] value = annotatedByValuable.value();
        if (value.length > 0) {
            return ArrayUtils.toArray(value);
        }
        return CartesianUtil.cartesian_single_def(Character.class);
    }
}