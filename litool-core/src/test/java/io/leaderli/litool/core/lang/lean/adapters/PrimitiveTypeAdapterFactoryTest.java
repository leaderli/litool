package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.type.LiTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PrimitiveTypeAdapterFactoryTest {

    @Test
    void test() {

        Lean lean = null;

        TypeAdapter<Integer> typeAdapter = new PrimitiveTypeAdapterFactory().create(null, LiTypeToken.of(Integer.class));

        Assertions.assertEquals(1, typeAdapter.read("1", lean));
        Assertions.assertEquals(0, typeAdapter.read(null, lean));
        Assertions.assertEquals(1, typeAdapter.read(1, lean));

    }
}
