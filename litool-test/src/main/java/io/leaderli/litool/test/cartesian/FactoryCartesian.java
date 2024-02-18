package io.leaderli.litool.test.cartesian;

import io.leaderli.litool.core.type.ReflectUtil;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class FactoryCartesian implements CartesianFunction<FactoryValues, Object> {
    @Override
    public Object[] apply(FactoryValues annotatedByValuable, CartesianContext context) {

        return ReflectUtil.newInstance(annotatedByValuable.value())
                .map(f -> f.apply(context))
                .or(new Object[0])
                .get();
    }
}
