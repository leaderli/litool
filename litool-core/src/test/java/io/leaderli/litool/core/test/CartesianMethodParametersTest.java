package io.leaderli.litool.core.test;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author leaderli
 * @since 2022/8/21
 */
class CartesianMethodParametersTest {


    Method method = ReflectUtil.getMethod(getClass(), "have").get();

    CartesianContext context = new CartesianContext();

    @Test
    void test() {


        CartesianMethodParameters cartesianMethodParameters = new CartesianMethodParameters(method, context);
        Lira<Object[]> parametersLira = cartesianMethodParameters.cartesian();

        System.out.println(parametersLira.size());
        for (Object[] parameters : parametersLira) {
            System.out.println(Arrays.toString(parameters));
            Assertions.assertTrue(ReflectUtil.getMethodValue(method, this, parameters).present());
        }

    }

    public int have(@IntValues({10, 20}) int length, @IntValues({3, 4}) int width, @ObjectValues Bean v) {
        return length * width;
    }

    private static class Bean {

    }
}
