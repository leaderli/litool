package io.leaderli.litool.core.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * @author leaderli
 * @since 2022/8/17 7:30 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Valuable {

    /**
     * @return 一个提供转换函数的元注解，被该注解的注解，仅第一个会生效
     * @see CartesianUtil#cartesian(Field)
     */
    Class<? extends CartesianFunction<?, ?>> value();


}
