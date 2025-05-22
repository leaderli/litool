package io.leaderli.litool.test;

import io.leaderli.litool.core.type.MethodFilter;
import io.leaderli.litool.core.type.MethodUtil;
import io.leaderli.litool.core.util.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

class SkipClassInitializerTest {

    @Test
    void test1() {
        LiMock.mockStatic(MethodUtil.class, MethodFilter.name("onlyCallByCLINIT"), MethodProxy.NONE);
        AtomicInteger error = new AtomicInteger();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    LiMock.skipClassInitializer(Error.class);
                } catch (Throwable e) {
                    error.incrementAndGet();
                }
            }).start();
        }
        ThreadUtil.sleep(1000);
        Assertions.assertEquals(0, error.get());
    }

    @SuppressWarnings("ConstantValue")
    static class Error {
        static {
            if (1 == 1) {
                throw new IllegalStateException("1 == 1");
            }
        }

        public static int m1() {
            return 1;
        }

        public static int m1(int a) {
            return a + 1;
        }

        public static int m3() {
            return m4();
        }

        private static int m4() {
            return 4;
        }


    }
}
