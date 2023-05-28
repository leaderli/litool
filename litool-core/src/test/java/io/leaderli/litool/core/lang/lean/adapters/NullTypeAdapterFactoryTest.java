package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NullTypeAdapterFactoryTest {

    @Test
    void test() {

        TypeAdapter<Object> nullTypeAdapterFactory = new NullTypeAdapterFactory().create(null, null);
        Assertions.assertSame(new NullTypeAdapterFactory().create(null, null), nullTypeAdapterFactory);

        Assertions.assertNull(nullTypeAdapterFactory.read(null, null));
        Assertions.assertNull(nullTypeAdapterFactory.read(123, new Lean()));

    }

}
