package io.leaderli.litool.core.test;

import io.leaderli.litool.core.type.PrimitiveEnum;

/**
 * @author leaderli
 * @since 2022/8/20
 */
class DoubleCartesian implements CartesianFunction<DoubleValues, Double> {
@Override
public Double[] apply(DoubleValues intValues, CartesianContext context) {
    double[] value = intValues.value();
    if (value.length > 0) {
        return (Double[]) PrimitiveEnum.toWrapperArray(value);
    }
    return CartesianUtil.cartesian(Double.class);
}
}
