package io.leaderli.litool.test.cartesian;

import org.apiguardian.api.API;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@API(status = API.Status.STABLE)
public @interface MockInit {
    /**
     * the mock-method called before the {@link CartesianTest} test-method. the mock detail is achieved by {@link  CartesianMock}
     *
     * @return the mock-method name, default is 'init'
     * @see CartesianMock
     */
    String value() default "init";
}

