package io.leaderli.litool.core.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/17 7:36 PM
 */
class CartesianObjectTest {
    @Test
//    @ValueSource
    void cartesian() {
        CartesianObject<A> cartesian = new CartesianObject<>(A.class, field -> CartesianUtil.cartesian(field, new CartesianContext()));

        Assertions.assertTrue(cartesian.cartesian().size() > 0);


    }

    static class A {

        private final boolean fuck = false;
        public boolean hello;
        @StringValues("123")
        private String name;
        @DoubleValues({-1, 18, 100})
        private int age;
        private boolean red;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public boolean isRed() {
            return red;
        }

        public void setRed(boolean red) {
            this.red = red;
        }
    }
}
