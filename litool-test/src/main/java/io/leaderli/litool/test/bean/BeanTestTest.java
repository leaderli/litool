package io.leaderli.litool.test.bean;

import io.leaderli.litool.core.function.GetSet;
import org.junit.jupiter.api.Assertions;

public class BeanTestTest {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @BeanTest
    public void test(GetSet getSet) throws Throwable {
        Assertions.assertDoesNotThrow(() -> getSet.set(getSet.get()));
    }

    private static class Bean {
        private String name = "1";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
}
