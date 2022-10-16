package io.leaderli.litool.test;

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
     * the annotation mock method called in the {@link LiTest} annotated
     * method. the mock is achieved by {@link  LiMock}
     *
     * @return the mock method name, default is 'init'
     * @see LiMock
     */
    String value() default "init";
}

