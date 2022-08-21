package io.leaderli.litool.core.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/8/17 7:32 PM
 */

@Retention(RetentionPolicy.RUNTIME)
@Valuable(FactoryCartesian.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface FactoryValues {
    Class<? extends Function<CartesianContext, Object[]>> value();
}

