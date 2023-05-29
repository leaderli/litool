package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.type.LiTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringTypeAdapterFactoryTest {


    @Test
    void test() {
        StringTypeAdapterFactory typeAdapterFactory = new StringTypeAdapterFactory();

        TypeAdapter<String> typeAdapter = typeAdapterFactory.create(null, LiTypeToken.of(String.class));

        Assertions.assertEquals("123", typeAdapter.read("123", null));
        typeAdapter = typeAdapterFactory.create(null, LiTypeToken.of(CharSequence.class));
        Assertions.assertEquals("123", typeAdapter.read("123", null));


        Lean lean = new Lean();
        Assertions.assertEquals("1", lean.fromBean("1", String.class));
        Assertions.assertNull(lean.fromBean(null, String.class));

    }


}
