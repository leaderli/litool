package io.leaderli.litool.test;

import io.leaderli.litool.core.test.CartesianContext;
import io.leaderli.litool.core.test.DynamicValues;
import io.leaderli.litool.core.test.IntValues;
import org.junit.jupiter.api.Assertions;

@MockContext
public class MockContextTest {
    static void context(CartesianContext context) {
        context.registerCustomValuable(IntValues.class, (type, annotation, annotatedElement, context1) -> new Object[]{100});
    }


    @LiTest
    void test2(@IntValues({1, 2, 3}) int age, @DynamicValues("test") Object obj) {

        Assertions.assertEquals(100, age);
    }

}
