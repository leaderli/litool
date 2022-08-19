package io.leaderli.litool.core.cartesian;

import com.google.gson.Gson;
import io.leaderli.litool.core.test.Cartesian;
import io.leaderli.litool.core.test.IntValues;
import io.leaderli.litool.core.test.Valuable;
import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

/**
 * @author leaderli
 * @since 2022/8/17 7:36 PM
 */
class CartesianTest {
    static class A {

        private String name;

        @IntValues({-1, 18, 100})
        private int age;
        private boolean red;
        public boolean hello;
        private final boolean fuck = false;

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

    @Test
    void test() {
        for (Field declaredField : A.class.getDeclaredFields()) {


            System.out.println(ReflectUtil.findAnnotationsWithMark(declaredField, Valuable.class));

        }

    }

//    @Test
//    @ValueSource
    void cartesian() {
        Cartesian<A> cartesian = new Cartesian<>(A.class);

        Gson gson = new Gson();
        for (A a : cartesian.cartesian()) {

            System.out.println(gson.toJson(a));

        }

    }
}
