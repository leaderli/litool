package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.bean.Person;
import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.type.LiTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class MapTypeAdapterFactoryTest {

    @Test
    void test() {
        Lean lean = new Lean();

        MapTypeAdapterFactory mapTypeAdapterFactory = new MapTypeAdapterFactory();
        TypeAdapter<Map<String, String>> mapTypeAdapter = mapTypeAdapterFactory.create(lean, new LiTypeToken<Map<String, String>>() {
        });

        Person person = new Person();
        person.setName("tom");
        Map<String, String> read = mapTypeAdapter.read(person, lean);
        Assertions.assertEquals("tom", read.get("name"));
        Assertions.assertEquals("0", read.get("age"));
    }
}
