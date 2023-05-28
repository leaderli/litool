package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.type.LiTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ArrayTypeAdapterFactoryTest {

    @Test
    void test() {

        Lean lean = new Lean();

        TypeAdapter<String[]> typeAdapter = new ArrayTypeAdapterFactory().create(lean, LiTypeToken.of(String[].class));

        Assertions.assertArrayEquals(new String[]{"1", "2", "3"}, typeAdapter.read(CollectionUtils.toList("1", "2", "3"), lean));
        Assertions.assertArrayEquals(new String[]{}, typeAdapter.read(null, lean));

    }

}
