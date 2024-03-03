package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

class MethodFilterTest {


    @Test
    void test() throws NoSuchMethodException {
        Method method = Object.class.getMethod("wait");

        Assertions.assertTrue(MethodFilter.isMethod().apply(method));
        Assertions.assertTrue(MethodFilter.declare(Object.class).apply(method));
        Assertions.assertFalse(MethodFilter.name("fuck").apply(method));
        Assertions.assertTrue(MethodFilter.length(0).apply(method));

        method = List.class.getMethod("add", int.class, Object.class);
        Assertions.assertTrue(MethodFilter.parameterType(int.class, Object.class).apply(method));

    }
}
