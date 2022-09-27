package io.leaderli.litool.core.test;

import io.leaderli.litool.core.collection.CollectionUtils;

/**
 * @author leaderli
 * @since 2022/8/20
 */
class CharCartesian implements CartesianFunction<CharValues, Character> {
    @Override
    public Character[] apply(CharValues annotatedByValuable, CartesianContext context) {
        char[] value = annotatedByValuable.value();
        if (value.length > 0) {
            return CollectionUtils.toArray(value);
        }
        return CartesianUtil.cartesian_single_def(Character.class);
    }
}
