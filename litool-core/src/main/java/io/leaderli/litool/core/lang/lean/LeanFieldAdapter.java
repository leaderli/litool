package io.leaderli.litool.core.lang.lean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author leaderli
 * @since 2022/9/27 11:53 AM
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface LeanFieldAdapter {
    Class<? extends TypeAdapter<?>> value();
}
