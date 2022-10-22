package io.leaderli.litool.core.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * get values by  call the method on the test class
 *
 * @author leaderli
 * @since 2022/8/17 7:32 PM
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface DynamicValues {
    String value();
}

