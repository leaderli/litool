package io.leaderli.litool.core.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LiCache {

    long millis() default 5000;

    boolean allowNullValue() default false;

    boolean ignoreError() default false;

}
