package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.exception.LiAssertException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/13 2:46 PM
 */
class BeanPathTest {


    @Test
    public void test() {


        Assertions.assertThrows(LiAssertException.class, () -> BeanPath.of("."));
        Assertions.assertThrows(LiAssertException.class, () -> BeanPath.of("[123"));
        Assertions.assertThrows(LiAssertException.class, () -> BeanPath.of("[123]["));
        Assertions.assertThrows(IllegalStateException.class, () -> BeanPath.of("[123."));

        BeanPath of = BeanPath.of("123");

        Map<String, Object> origin = new HashMap<>();
        origin.put("123", "123");
        origin.put("list", Arrays.asList(1, 2, 3));
        System.out.println(of.parser(origin));

    }

}
