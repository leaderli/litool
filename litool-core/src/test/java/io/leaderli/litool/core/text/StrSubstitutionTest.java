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
void test() {

    Map<String, Object> map = new HashMap<>();
    map.put("a", true);
    map.put("cc", true);
    map.put("b", "b");

    Assertions.assertEquals("}", StrSubstitution.replace("}"));
    Assertions.assertEquals("}}", StrSubstitution.replace("}}"));
    Assertions.assertEquals("{}", StrSubstitution.replace("{}"));
    Assertions.assertEquals("{}", StrSubstitution.replace("{{}"));
    Assertions.assertEquals("{", StrSubstitution.replace("{{"));
    Assertions.assertEquals("{}}", StrSubstitution.replace("{}}"));
    Assertions.assertEquals("a true b", StrSubstitution.replace("a {a} {b}", map));
    Assertions.assertEquals("a true b", StrSubstitution.replace("a {a} {b}", true, 'b'));
    Assertions.assertEquals("a a a", StrSubstitution.replace("a {a} {a}", 'a', 'b'));
    Assertions.assertEquals("a a b ", StrSubstitution.replace("a {a} {b} {c}", 'a', 'b'));
    Assertions.assertEquals("true", StrSubstitution.replace("{cc}", map));
    Assertions.assertEquals("a", StrSubstitution.replace("{aa}", 'a'));


}

}
