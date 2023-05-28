package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.type.LiTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReflectTypeAdapterFactoryTest {

    @Test
    void test() {
        ReflectTypeAdapterFactory reflectTypeAdapterFactory = new ReflectTypeAdapterFactory();

        Lean lean = new Lean();
        Assertions.assertNull(reflectTypeAdapterFactory.create(lean, LiTypeToken.of(None.class)));


    }


    private interface None {

    }
}
