package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
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


    }

    @Test
    void scan() {
        Set<Class<?>> scan = new ClassScanner(this.getClass().getPackage().getName()).scan();
        Assertions.assertTrue(scan.size() > 10);
    }

    @Test
    void testScan() {
    }

    @SuppressWarnings("rawtypes")
    @Test
    void getSubTypesOf() {

        Lira<Class<Lino>> subTypesOf = ClassScanner.getSubTypesOf(Lino.class.getPackage().getName(), Lino.class);
        Assertions.assertEquals(2, subTypesOf.size());
    }
}
