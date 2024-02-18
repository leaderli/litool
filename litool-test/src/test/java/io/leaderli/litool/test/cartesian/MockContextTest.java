package io.leaderli.litool.test.cartesian;

import org.junit.jupiter.api.Assertions;

@MockContext
public class MockContextTest {
    static void context(CartesianContext context) {
        context.registerCustomValuable(IntValues.class, (type, annotation, annotatedElement, context1) -> new Object[]{100});
    }


    @CartesianTest
    void test2(@IntValues({1, 2, 3}) int age) {

        Assertions.assertEquals(100, age);
    }

}
