package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.LeanValue;
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

        Assertions.assertArrayEquals(new String[]{"1", "2", "3"}, lean.fromBean(CollectionUtils.toList("1", "2", "3"), String[].class));
        Assertions.assertArrayEquals(new String[]{}, lean.fromBean(null, String[].class));
        Assertions.assertArrayEquals(new Integer[]{0, 123}, lean.fromBean(new String[]{null, "123"}, Integer[].class));
    }

    @Test
    void test1() {
        Assertions.assertArrayEquals(new Integer[]{0, 123}, new Lean().fromBean(new String[]{null, "123"}, Integer[].class));

    }

    private static class Bean {
        @LeanValue(DoubleTypeAdapter.class)
        private int custom;


    }

    private static class DoubleTypeAdapter implements TypeAdapter<Integer> {

        @Override
        public Integer read(Object source, Lean lean) {
            return null;
        }

        @Override
        public Integer read(Lean lean) {
            return 10086;
        }
    }
}
