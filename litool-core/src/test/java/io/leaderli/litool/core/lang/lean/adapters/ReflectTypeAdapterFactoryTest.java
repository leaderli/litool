package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.type.LiTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.AbstractList;
import java.util.List;

class ReflectTypeAdapterFactoryTest {

    @Test
    void test() {
        ReflectTypeAdapterFactory reflectTypeAdapterFactory = new ReflectTypeAdapterFactory();

        Lean lean = new Lean();
        Assertions.assertNull(reflectTypeAdapterFactory.create(lean, LiTypeToken.of(None.class)));
        Assertions.assertNull(reflectTypeAdapterFactory.create(lean, LiTypeToken.of(Integer.class)));
        Assertions.assertNull(reflectTypeAdapterFactory.create(lean, LiTypeToken.of(Color.class)));
        Assertions.assertNull(reflectTypeAdapterFactory.create(lean, LiTypeToken.of(String[].class)));
        Assertions.assertNull(reflectTypeAdapterFactory.create(lean, LiTypeToken.of(List.class)));
        Assertions.assertNull(reflectTypeAdapterFactory.create(lean, LiTypeToken.of(AbstractList.class)));


    }


    private interface None {

    }
}
