package io.leaderli.litool.core.test;

import io.leaderli.litool.core.type.PrimitiveEnum;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class LongCartesian implements CartesianFunction<LongValues, Long> {
@Override
public Long[] apply(LongValues longValues, CartesianContext context) {
    long[] value = longValues.value();
    if (value.length > 0) {
        return (Long[]) PrimitiveEnum.toWrapperArray(value);
    }
    return CartesianUtil.cartesian(Long.class);
}
}
