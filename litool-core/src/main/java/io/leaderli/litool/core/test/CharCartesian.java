package io.leaderli.litool.core.test;

import io.leaderli.litool.core.type.PrimitiveEnum;

/**
 * @author leaderli
 * @since 2022/8/20
 */
class CharCartesian implements CartesianFunction<CharValues, Character> {
    @Override
    public Character[] apply(CharValues charValues, CartesianContext context) {
        char[] value = charValues.value();
        if (value.length > 0) {
            return (Character[]) PrimitiveEnum.toWrapperArray(value);
        }
        return CartesianUtil.cartesian(Character.class);
    }
}
