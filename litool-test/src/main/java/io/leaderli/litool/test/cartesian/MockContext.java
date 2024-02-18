package io.leaderli.litool.test.cartesian;

import org.apiguardian.api.API;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@API(status = API.Status.STABLE)
public @interface MockContext {

    /**
     * the context-method called before the {@link CartesianTest} test-method and it's called before {@link MockInit}.
     * <p>
     * it's use to setup {@link  CartesianContext}, the context-method must have one parameter of the {@link  CartesianContext}
     *
     * <pre>
     * {@code
     *     static void context(CartesianContext context) {
     *         context.registerCustomValuable(IntValues.class, (type, annotation, annotatedElement, context1) -> new Object[]{100});
     *     }
     * }
     *
     * </pre>
     *
     * @return the init context-method name, default is 'context'
     * @see LiMock
     */
    String value() default "context";
}

