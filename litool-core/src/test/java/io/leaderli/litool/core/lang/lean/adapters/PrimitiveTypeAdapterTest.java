package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.type.LiTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PrimitiveTypeAdapterTest {

    @Test
    void test() {

        PrimitiveTypeAdapterFactory primitiveTypeAdapterFactory = new PrimitiveTypeAdapterFactory();

        TypeAdapter<Boolean> typeAdapter = primitiveTypeAdapterFactory.create(null, LiTypeToken.of(boolean.class));

        Assertions.assertTrue(typeAdapter.read("true", null));
        Assertions.assertFalse(typeAdapter.read("false", null));

    }
}
