package io.leaderli.litool.core.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/14
 */
@SuppressWarnings("CollectionAddedToSelf")
class StrSubstitutionTest {


    @Test
    void format() {

        Assertions.assertEquals("123", StrSubstitution.$format("123"));
        Assertions.assertEquals("123$", StrSubstitution.$format("123$"));
        Assertions.assertEquals("123${", StrSubstitution.$format("123${"));
        Assertions.assertEquals("123${1", StrSubstitution.$format("123${1"));
        Assertions.assertEquals("123$1", StrSubstitution.$format("123$${1}", (k, v) -> k));
        Assertions.assertEquals("123{}#", StrSubstitution.parse("123{}#", "{", "}#", (k, v) -> k));
        Assertions.assertEquals("1234", StrSubstitution.parse("123{4}#", "{", "}#", (k, v) -> k));
        Assertions.assertEquals("123{4}", StrSubstitution.parse("123{4}", "{", "}#", (k, v) -> k));
        Assertions.assertEquals("1234}", StrSubstitution.parse("123{4}}#", "{", "}#", (k, v) -> k));


        Assertions.assertEquals("${}${}${}", StrSubstitution.$format("${}${}${}"));
        Assertions.assertEquals("${}${}", StrSubstitution.$format("${}${}"));
        Assertions.assertEquals("$1", StrSubstitution.$format("$1"));
        Assertions.assertEquals("", StrSubstitution.format(null));
        Assertions.assertEquals("}", StrSubstitution.format("}"));
        Assertions.assertEquals("}}", StrSubstitution.format("}}"));
        Assertions.assertEquals("{}", StrSubstitution.format("{}"));
        Assertions.assertEquals("{{}", StrSubstitution.format("{{}"));
        Assertions.assertEquals("{}}", StrSubstitution.format("{}}"));
        Assertions.assertEquals("{", StrSubstitution.format("{"));
        Assertions.assertEquals("{123", StrSubstitution.format("{123"));

        Assertions.assertEquals("$$}", StrSubstitution.$format("$$}", (k, v) -> k));
        Assertions.assertEquals("$$", StrSubstitution.$format("$$", (k, v) -> k));
        Assertions.assertEquals("$}", StrSubstitution.$format("$}", (k, v) -> k));
        Assertions.assertEquals("${}", StrSubstitution.$format("${}", (k, v) -> k));
        Assertions.assertEquals("$x", StrSubstitution.$format("$${x}", (k, v) -> k));
        Assertions.assertEquals("${123}", StrSubstitution.parse("${#{x}}", "#{", "}", (k, v) -> 123));


        Assertions.assertEquals("a true b", StrSubstitution.format("a {a} {b}", true, 'b'));
        Assertions.assertEquals("a a a", StrSubstitution.format("a {a} {a}", 'a', 'b'));
        Assertions.assertEquals("a a b {c}", StrSubstitution.format("a {a} {b} {c}", 'a', 'b'));
        Assertions.assertEquals("a", StrSubstitution.format("{aa}", 'a'));

    }

    @Test
    void parse() {
        Assertions.assertEquals("%%", StrSubstitution.parse("%%", "%", "%", (k, v) -> 1));
        Assertions.assertEquals("%%%", StrSubstitution.parse("%%%", "%", "%", (k, v) -> 1));
        Map<String, Object> map = new HashMap<>();
        map.put("a", "li");
        Assertions.assertEquals("li{b}", StrSubstitution.parse("${a}{b}", "$", "}", (name, def) -> {
            if (name.startsWith("{")) {
                return map.get(name.substring(1));
            }
            return name;
        }));
    }

    @Test
    void beanPath() {

        Map<String, Object> map = new HashMap<>();
        map.put("a", true);
        map.put("cc", true);
        map.put("b", "b");
        map.put("d", map);
        map.put("e", Arrays.asList(1, 2, 3));

        Assertions.assertEquals("a true b", StrSubstitution.beanPath("a {a} {b}", map));
        Assertions.assertEquals("true", StrSubstitution.beanPath("{cc}", map));
        Assertions.assertEquals("true", StrSubstitution.beanPath("{cc}", map));

        Assertions.assertEquals("b", StrSubstitution.beanPath("{d.d.b}", map));
        Assertions.assertEquals("2", StrSubstitution.beanPath("{e[1]}", map));
        Assertions.assertEquals("{e[3]}", StrSubstitution.beanPath("{e[3]}", map));

        Assertions.assertEquals("3", StrSubstitution.beanPath("{e[-1]}", map));
        Assertions.assertEquals("true", StrSubstitution.$beanPath("${cc}", map));


    }

}
