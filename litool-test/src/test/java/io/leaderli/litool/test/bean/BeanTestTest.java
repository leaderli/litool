package io.leaderli.litool.test.bean;

import org.junit.jupiter.api.Assertions;

public class BeanTestTest {

    @BeanTest(value = "test.(?!bean.)", scan = "io.leaderli.litool.test")
    public void test(BeanMethod beanMethod) throws Throwable {
        Assertions.assertTrue(beanMethod.toString().startsWith("i.l.l.t.b"));
        Assertions.assertDoesNotThrow(beanMethod.supplier::get);
    }

    @SuppressWarnings("FieldCanBeLocal")
    private static class Bean {
        private String name = "1";
        /**
         * 测试只有get
         */
        private int age;
        /**
         * 测试只有set
         */
        private Integer gender;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return this.age;
        }

        public void setGender(Integer gender) {
            this.gender = gender;
        }

        private void privateMethod() {
            this.age = 11;
        }

        @SuppressWarnings("InnerClassMayBeStatic")
        private class Inner {
            private String name = "1";

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

    }

    private static class Ignore1 {

    }

    private enum TestEnum {
        A, B;

        int age() {
            return 1;
        }


    }
}
