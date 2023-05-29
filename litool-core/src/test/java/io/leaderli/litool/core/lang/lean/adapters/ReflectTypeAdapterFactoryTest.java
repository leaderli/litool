package io.leaderli.litool.core.lang.lean.adapters;

import com.google.gson.Gson;
import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.type.LiTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.AbstractList;
import java.util.List;
import java.util.Map;

class ReflectTypeAdapterFactoryTest {
    Gson gson = new Gson();

    @Test
    void test() {
        ReflectTypeAdapterFactory reflectTypeAdapterFactory = new ReflectTypeAdapterFactory();

        Lean lean = new Lean();
        Assertions.assertNull(reflectTypeAdapterFactory.create(lean, LiTypeToken.of(None.class)));
        Assertions.assertNull(reflectTypeAdapterFactory.create(lean, LiTypeToken.of(Integer.class)));
        Assertions.assertNull(reflectTypeAdapterFactory.create(lean, LiTypeToken.of(Color.class)));
        Assertions.assertNull(reflectTypeAdapterFactory.create(lean, LiTypeToken.of(String[].class)));
        Assertions.assertNull(reflectTypeAdapterFactory.create(lean, LiTypeToken.of(List.class)));
        Assertions.assertNull(reflectTypeAdapterFactory.create(lean, LiTypeToken.of(AbstractList.class)));

        String json = "{\"name\":\"1\",\"bean3\": {\"name\": \"2\",\"bean2\": {\"name\": \"3\"}}}";
        Bean2 bean2 = lean.fromBean(gson.fromJson(json, Map.class), Bean2.class);
        Assertions.assertEquals("3", bean2.bean3.bean2.name);

    }


    private interface None {

    }

    private static class Bean2 {
        private Bean3 bean3;
        private String name;

    }

    private static class Bean3 {
        private String name;
        private Bean2 bean2;
    }
}
