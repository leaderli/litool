package io.leaderli.litool.test.cartesian;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

/**
 * @author leaderli
 * @since 2022/8/17 7:30 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Valuable {

    /**
     * @return a meta annotation that provide a {@link  CartesianFunction}
     * @see CartesianUtil#cartesian(Field, CartesianContext)
     * @see CartesianUtil#cartesian(Parameter, CartesianContext)
     */
    Class<? extends CartesianFunction<?, ?>> value();


}
