package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.test.CartesianContext;
import io.leaderli.litool.core.test.CartesianFunction;
import io.leaderli.litool.core.test.CartesianUtil;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

/**
 * @author leaderli
 * @since 2022/8/17 7:30 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface LeanFieldMetaAnnotation {

    /**
     * @return a meta annotation that provide a {@link  CartesianFunction}
     * @see CartesianUtil#cartesian(Field, CartesianContext)
     * @see CartesianUtil#cartesian(Parameter, CartesianContext)
     */
    Class<? extends LeanFieldFunction<? extends Annotation>> value();


}
