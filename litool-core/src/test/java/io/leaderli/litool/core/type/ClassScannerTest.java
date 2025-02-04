package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import org.apiguardian.api.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.util.Set;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Repeatable(NotNulls.class)
@API(status = API.Status.STABLE)
@interface NotNull {

    String value();


}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE})
@interface NotNulls {
    NotNull[] value();

    String name() default "123";
}

/**
 * @author leaderli
 * @since 2022/7/20
 */
class ClassScannerTest {


    @Test
    void scan() {
        Set<Class<?>> scan =
                new ClassScanner(this.getClass().getPackage().getName()).scan();
        Assertions.assertTrue(scan.contains(NotNull.class));
        Assertions.assertTrue(scan.contains(NotNulls.class));
    }


    @SuppressWarnings("rawtypes")
    @Test
    void getSubTypesOf() {

        Lira<Class<Lino>> subTypesOf =
                ClassScanner.getSubTypesOf(Lino.class.getPackage().getName(),
                        Lino.class);
        Assertions.assertEquals(3, subTypesOf.size());
    }


}
