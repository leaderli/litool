package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.type.LiTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class CollectionTypeAdapterFactoryTest {
    @Test
    void test() {

        Lean lean = new Lean();

        TypeAdapter<List<String>> typeAdapter = new CollectionTypeAdapterFactory().create(lean, LiTypeToken.getParameterized(List.class, String.class));

        Assertions.assertEquals(CollectionUtils.toList("1", "2", "3"), typeAdapter.read(CollectionUtils.toList("1", "2", "3"), lean));
        Assertions.assertEquals(CollectionUtils.toList(), typeAdapter.read(null, lean));

    }
}
