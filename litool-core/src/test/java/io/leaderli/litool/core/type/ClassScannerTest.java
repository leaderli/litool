package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * @author leaderli
 * @since 2022/7/20
 */
class ClassScannerTest {


    @Test
    void test() {
        Set<Class<?>> scan = new ClassScanner(this.getClass().getPackage().getName()).scan();

        Assertions.assertTrue(scan.size() > 10);

    }
}
