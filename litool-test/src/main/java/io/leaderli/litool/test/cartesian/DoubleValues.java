package io.leaderli.litool.test.cartesian;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author leaderli
 * @since 2022/8/17 7:32 PM
 */

@Retention(RetentionPolicy.RUNTIME)
@Valuable(DoubleCartesian.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface DoubleValues {
    double[] value();
}
