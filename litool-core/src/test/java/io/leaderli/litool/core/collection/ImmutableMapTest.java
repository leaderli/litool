package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/24
 */
class ImmutableMapTest {

    @Test
    void of() {


        Map<String, String> src = new HashMap<>();
        src.put("1", "1");

        ImmutableMap<String, String> immutableMap = ImmutableMap.of(src);
        src.put("2", "2");

        Assertions.assertNull(immutableMap.get("2"));

        src.remove("1");
        Assertions.assertEquals("1", immutableMap.get("1"));


        Assertions.assertDoesNotThrow(() -> ImmutableMap.of(null));
    }

}
