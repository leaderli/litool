package io.leaderli.litool.core.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/14
 */
class StrSubstitutionTest {

    @Test
    void format() {


        Assertions.assertEquals("", StrSubstitution.format(null));
        Assertions.assertEquals("}", StrSubstitution.format("}"));
        Assertions.assertEquals("}}", StrSubstitution.format("}}"));
        Assertions.assertEquals("{}", StrSubstitution.format("{}"));
        Assertions.assertEquals("{}", StrSubstitution.format("{{}"));
        Assertions.assertEquals("{", StrSubstitution.format("{{"));
        Assertions.assertEquals("{}}", StrSubstitution.format("{}}"));

        Assertions.assertEquals("a true b", StrSubstitution.format("a {a} {b}", true, 'b'));
        Assertions.assertEquals("a a a", StrSubstitution.format("a {a} {a}", 'a', 'b'));
        Assertions.assertEquals("a a b {c}", StrSubstitution.format("a {a} {b} {c}", 'a', 'b'));
        Assertions.assertEquals("a", StrSubstitution.format("{aa}", 'a'));

        Map<String, Object> map = new HashMap<>();
        map.put("a", true);
        map.put("cc", true);
        map.put("b", "b");
        Assertions.assertEquals("a true b", StrSubstitution.format("a {a} {b}", map));
        Assertions.assertEquals("true", StrSubstitution.format("{cc}", map));

    }

}
