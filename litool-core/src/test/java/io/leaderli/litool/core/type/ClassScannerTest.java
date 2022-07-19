package io.leaderli.litool.core.type;

import io.leaderli.litool.core.collection.LiIterator;
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

        for (Class<?> aClass : LiIterator.of(scan)) {
            System.out.println(aClass);
        }

    }
}
