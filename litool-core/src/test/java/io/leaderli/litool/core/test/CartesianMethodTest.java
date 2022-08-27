package io.leaderli.litool.core.test;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * @author leaderli
 * @since 2022/8/21
 */
class CartesianMethodTest {


Method method = ReflectUtil.getMethod(getClass(), "have").get();

CartesianContext context = new CartesianContext();

@Test
void test() {


    CartesianMethod cartesianMethod = new CartesianMethod(method, context);
    Lira<Object[]> parametersLira = cartesianMethod.cartesian();

    for (Object[] parameters : parametersLira) {
        Assertions.assertDoesNotThrow(() -> ReflectUtil.getMethodValue(method, this, parameters));
    }

}

public int have(@IntValues({10, 20}) int length, @IntValues({3, 4}) int width, @ObjectValues Class<?> ignore) {
    return length * width;
}
}
