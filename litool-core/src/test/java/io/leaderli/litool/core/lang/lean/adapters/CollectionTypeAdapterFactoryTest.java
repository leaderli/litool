package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.type.LiTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

class CollectionTypeAdapterFactoryTest {
    @Test
    void test() {

        Lean lean = new Lean();

        LiTypeToken<List<String>> liTypeToken = LiTypeToken.ofParameterized(List.class, String.class);
        TypeAdapter<List<String>> typeAdapter = new CollectionTypeAdapterFactory().create(lean, liTypeToken);

        Assertions.assertEquals(CollectionUtils.toList("1", "2", "3"), typeAdapter.read(CollectionUtils.toList("1", "2", "3"), lean));
        Assertions.assertEquals(CollectionUtils.toList(), typeAdapter.read(null, lean));

        TypeAdapter<Set<String>> typeAdapterSet = new CollectionTypeAdapterFactory().create(lean, LiTypeToken.ofParameterized(Set.class, Integer.class));
        Assertions.assertEquals(CollectionUtils.toSet(), typeAdapterSet.read(null, lean));
        Assertions.assertEquals(CollectionUtils.toSet(1, 2).toString(), typeAdapterSet.read(CollectionUtils.toList(1, 2, 1), lean).toString());


        Assertions.assertEquals(CollectionUtils.toList("1", "2", "3"), lean.fromBean(CollectionUtils.toList("1", "2", "3"), liTypeToken));
        Assertions.assertEquals(CollectionUtils.toList(), lean.fromBean(null, liTypeToken));

        Assertions.assertEquals(CollectionUtils.toSet(), lean.fromBean(null, LiTypeToken.ofParameterized(Set.class)));
        Assertions.assertEquals(CollectionUtils.toSet(1, 2), lean.fromBean(CollectionUtils.toList(1, 2, 1), LiTypeToken.ofParameterized(Set.class, Integer.class)));
    }
}
