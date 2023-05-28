package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReflectTypeAdapterFactoryTest {

    @Test
    void test() {
        ReflectTypeAdapterFactory reflectTypeAdapterFactory = new ReflectTypeAdapterFactory();

        Lean lean = new Lean();
        Assertions.assertNull(reflectTypeAdapterFactory.create(lean, LiTypeToken.of(None.class)));
        Lino<None> noneLino = ReflectUtil.newInstance(None.class);

        Assertions.assertThrows(IllegalArgumentException.class, () -> lean.fromBean(123, None.class));


    }


    private interface None {

    }
}
