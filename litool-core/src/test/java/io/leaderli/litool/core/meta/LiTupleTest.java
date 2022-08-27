package io.leaderli.litool.core.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/17
 */
class LiTupleTest {

@Test
void haveNull() {
    LiTuple1<String> tu = LiTuple.of(null);
    Assertions.assertFalse(tu.notIncludeNull());
    tu = tu.update1("1");
    Assertions.assertTrue(tu.notIncludeNull());

    LiTuple2<String, Object> append = tu.append(null);
    Assertions.assertFalse(append.notIncludeNull());
    append = append.update2("2");
    Assertions.assertTrue(append.notIncludeNull());

}
}
