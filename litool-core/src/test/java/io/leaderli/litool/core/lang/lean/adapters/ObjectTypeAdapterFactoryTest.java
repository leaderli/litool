package io.leaderli.litool.core.lang.lean.adapters;

import com.google.gson.Gson;
import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.type.LiTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

class ObjectTypeAdapterFactoryTest {
    Gson gson = new Gson();

    @Test
    void test() {
        String json = "{\"name\": [\"123\"],\"ages\": [10,18]}";

        Lean lean = new Lean();
        Bean<Integer> bean = lean.fromBean(gson.fromJson(json, Map.class), LiTypeToken.ofParameterized(Bean.class, Integer.class));

        ObjectTypeAdapterFactory.ObjectTypeAdapter typeAdapter = new ObjectTypeAdapterFactory.ObjectTypeAdapter();
        Assertions.assertEquals(1, typeAdapter.read(1, lean));
        Assertions.assertTrue(typeAdapter.read(new HashMap<>(), lean) instanceof Map);
        Assertions.assertTrue(typeAdapter.read(new ArrayList<>(), lean) instanceof List);
        Assertions.assertTrue(typeAdapter.read(new LinkedList<>(), lean) instanceof List);
        Object read = typeAdapter.read(new LinkedList<>(), lean);
        Assertions.assertTrue(read instanceof List);
        Assertions.assertSame(Integer[].class, bean.ages.getClass());
        Bean<Integer> copy = new Bean<>();
        lean.copyBean(bean, copy, LiTypeToken.ofParameterized(Bean.class, Integer.class));
        Assertions.assertArrayEquals(new String[]{"123"}, copy.name);
        Assertions.assertEquals(10, copy.ages[0]);
    }

    private static class Bean<T> {
        private String[] name;
        private T[] ages;
    }
}
