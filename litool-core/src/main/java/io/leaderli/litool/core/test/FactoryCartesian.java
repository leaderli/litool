package io.leaderli.litool.core.test;

import io.leaderli.litool.core.type.ReflectUtil;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class FactoryCartesian implements CartesianFunction<FactoryValues, Object> {
    @Override
    public Object[] apply(FactoryValues factoryValues, CartesianContext context) {
        return ReflectUtil.newInstance(factoryValues.value()).map(f -> f.apply(context)).get();
    }
}
