package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

class CustomTypeAdapterFactoryTest {

    @Test
    void test() {
        Lean lean = new Lean();

        Assertions.assertNull(lean.fromBean(123, None.class));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Lean(null, null, true).fromBean(123, None.class));


        Lean stick = new Lean(null, null, true);

        stick.addCustomTypeAdapter((source, lean1) -> null, None.class);
        Assertions.assertDoesNotThrow(() -> stick.fromBean(123, None.class));

        Assertions.assertNotNull(stick.fromBean(System.currentTimeMillis(), Date.class));


        lean.addCustomTypeAdapter((source, lean1) -> new Hashtable<>(), Dictionary.class, Hashtable.class);

        Assertions.assertEquals(Hashtable.class, lean.fromBean(123, Dictionary.class).getClass());
        Assertions.assertEquals(Hashtable.class, lean.fromBean(123, Hashtable.class).getClass());
    }

    private interface None {

    }

}
