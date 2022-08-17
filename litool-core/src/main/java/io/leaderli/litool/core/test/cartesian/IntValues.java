package io.leaderli.litool.core.test.cartesian;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author leaderli
 * @since 2022/8/17 7:32 PM
 */

@Retention(RetentionPolicy.RUNTIME)
@Valuable
@Target(ElementType.FIELD)
public @interface IntValues {
    int[] value();
}
