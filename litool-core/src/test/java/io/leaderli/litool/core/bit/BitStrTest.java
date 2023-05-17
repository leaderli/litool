package io.leaderli.litool.core.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;


/**
 * @author leaderli
 * @since 2022/6/16 8:45 AM
 */
class BitStrTest {


    @Test
    void beauty() {


        BitStr bit = BitStr.of(TestMask.class);
        Assertions.assertEquals("C", bit.beauty(8));

        Assertions.assertEquals("", bit.beauty(16));
        Assertions.assertEquals("", bit.beauty(4));
        Assertions.assertEquals("C|B|A", bit.beauty(123));
        Assertions.assertEquals("A", bit.beauty(1));
        Assertions.assertEquals("", bit.beauty(0));

        Assertions.assertEquals("A", bit.beauty(0b001 | 0b100));
        Assertions.assertTrue(bit.toString().contains("0001"));


        Assertions.assertEquals("FINAL", BitStr.of(Modifier.class).beauty(Modifier.FINAL));


    }

    @Test
    void testToString() {
        Assertions.assertEquals("0000 0000 0000 0000 0000 0000 0000 0001 PUBLIC0000 0000 0000 0000 0000 0000 0000 " +
                        "0010 " +
                        "PRIVATE0000 0000 0000 0000 0000 0000 0000 0100 PROTECTED0000 0000 0000 0000 0000 0000 0000 " +
                        "1000 " +
                        "STATIC0000 0000 0000 0000 0000 0000 0001 0000 FINAL0000 0000 0000 0000 0000 0000 0010 0000 " +
                        "SYNCHRONIZED0000 0000 0000 0000 0000 0000 0100 0000 VOLATILE0000 0000 0000 0000 0000 0000 " +
                        "1000 " +
                        "0000 " +
                        "TRANSIENT0000 0000 0000 0000 0000 0001 0000 0000 NATIVE0000 0000 0000 0000 0000 0010 0000 " +
                        "0000 " +
                        "INTERFACE0000 0000 0000 0000 0000 0100 0000 0000 ABSTRACT0000 0000 0000 0000 0000 1000 0000 " +
                        "0000" +
                        " STRICT"
                , BitStr.of(Modifier.class).toString().replace(System.lineSeparator(), ""));
    }

    @SuppressWarnings("unused")
    public interface TestMask {

        int A = 0b1;
        int B = 0b10;
        int C = 0b1000;
    }

}
