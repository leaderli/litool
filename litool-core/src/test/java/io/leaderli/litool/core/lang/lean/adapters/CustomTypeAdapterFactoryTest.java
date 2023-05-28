package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.collection.LiMapUtil;
import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.type.InstanceCreator;
import io.leaderli.litool.core.type.LiTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class CustomTypeAdapterFactoryTest {

    @Test
    void test() {
        Lean lean = new Lean();

        Assertions.assertNull(lean.fromBean(123, None.class));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Lean(null, null, true).fromBean(123, None.class));

        lean = new Lean(LiMapUtil.newLinkedHashMap(None.class, (InstanceCreator<None>) type -> new None() {
        }), new ArrayList<>());
        Assertions.assertNotNull(lean.fromBean(123, None.class));

        Lean stick = new Lean(null, null, true);

        stick.getCustomTypeAdapterFactory().put(LiTypeToken.of(None.class), (TypeAdapter<None>) (source, lean1) -> null);
        Assertions.assertDoesNotThrow(() -> stick.fromBean(123, None.class));

    }

    private interface None {

    }

}