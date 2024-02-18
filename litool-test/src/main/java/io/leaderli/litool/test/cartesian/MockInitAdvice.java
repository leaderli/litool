package io.leaderli.litool.test.cartesian;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 *
 */
public class MockInitAdvice {
    /**
     * used to record the mocking class method has be called, and mark method potention return values by {@link  CartesianMock#when(Supplier, Object[])}
     * or disable some void-method call by {@link  CartesianMock#light(Runnable)}
     * <p>
     * this is a delegate for all method of mockindg class, will ignore actual inovation of all methods.
     * to prevent call actual method on the void-method, return a meaningless {@link CartesianMock#SKIP}.
     *
     * @param origin the origin method of mocking class
     * @return return a meaningless {@link CartesianMock#SKIP}
     */
    @SuppressWarnings("all")
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    public static Object enter(@Advice.Origin Method origin) {
        if (CartesianMock.onMockProgress) {
            CartesianMock.mockMethod = origin;
        }
        return CartesianMock.SKIP;
    }
}
