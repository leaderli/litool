package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.Lira;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * @author leaderli
 * @since 2022/7/26 8:47 AM
 */
class MethodScannerTest {

    @Test
    void test() {

        MethodScanner methodScanner = new MethodScanner(MethodScannerTest.class, true, null);

        Lira<Method> scan = methodScanner.scan();
        Assertions.assertEquals("test", scan.first().get().getName());



    }


}
