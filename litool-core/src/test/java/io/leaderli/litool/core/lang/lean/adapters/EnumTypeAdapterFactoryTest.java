package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.PrimitiveEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class EnumTypeAdapterFactoryTest {

    @Test
    void test() {

        TypeAdapter<PrimitiveEnum> typeAdapter = new EnumTypeAdapterFactory().create(null, LiTypeToken.of(PrimitiveEnum.class));

        assertSame(PrimitiveEnum.BOOLEAN, typeAdapter.read(PrimitiveEnum.BOOLEAN, null));
        assertSame(PrimitiveEnum.BOOLEAN, typeAdapter.read(PrimitiveEnum.BOOLEAN.name(), null));
        assertSame(null, typeAdapter.read(null, null));
        assertSame(PrimitiveEnum.BYTE, typeAdapter.read(0, null));


        Lean lean = new Lean();

        assertSame(PrimitiveEnum.BOOLEAN, lean.fromBean(PrimitiveEnum.BOOLEAN, PrimitiveEnum.class));
        assertSame(PrimitiveEnum.BOOLEAN, lean.fromBean(PrimitiveEnum.BOOLEAN.name(), PrimitiveEnum.class));
        assertSame(null, lean.fromBean(null, PrimitiveEnum.class));
        assertSame(PrimitiveEnum.BYTE, lean.fromBean(0, PrimitiveEnum.class));

    }
}
